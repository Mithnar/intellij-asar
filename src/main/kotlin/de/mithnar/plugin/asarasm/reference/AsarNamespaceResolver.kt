package de.mithnar.plugin.asarasm.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.*
import de.mithnar.plugin.asarasm.psi.mixin.AsarNamespaceEnterDirectiveMixin.Companion.NAMESPACE_NAME_TOKENS

/**
 * Resolves the active namespace stack at a given point in the file, and
 * produces candidate fully-prefixed label names to try during reference
 * resolution.
 *
 * Namespace semantics (from the Asar docs):
 *  - `namespace Foo` - push "Foo" onto the stack (when nested=on), or replace the current single namespace (nested=off)
 *  - `namespace off` - pop one level (nested=on) or clear (nested=off)
 *  - `namespace nested on/off` - toggle stacking behaviour
 *  - `pushns` / `pullns` - save / restore the entire namespace stack
 *
 * Candidate order for a simple name like "Main" inside Deep > Deeper:
 *   ["Deep_Deeper_Main", "Deep_Main", "Main"]
 */
object AsarNamespaceResolver {

    /**
     * Returns an ordered list of candidate fully-qualified label names for
     * [simpleName] at the position of [context], from most-specific to global.
     */
    fun buildCandidateNames(simpleName: String, context: PsiElement): List<String> {
        val stack = resolveStackAt(context)
        if (stack.isEmpty()) return listOf(simpleName)

        // Build candidates from most-specific to least-specific (global)
        val candidates = mutableListOf<String>()
        for (depth in stack.size downTo 1) {
            val prefix = stack.take(depth).joinToString("_")
            candidates.add("${prefix}_${simpleName}")
        }
        candidates.add(simpleName) // global fallback
        return candidates
    }

    /**
     * Returns the active namespace stack (ordered outermost → innermost) at
     * the position of [context] in its containing [AsarFile].
     */
    fun resolveStackAt(context: PsiElement): List<String> {
        val file = context.containingFile as? AsarFile ?: return emptyList()
        val contextOffset = context.textOffset

        var nestedEnabled = false
        val stack = ArrayDeque<String>()
        val savedStacks = ArrayDeque<Pair<Boolean, List<String>>>()

        for (child in file.children) {
            if (child.textOffset >= contextOffset) break
            nestedEnabled = processElement(child, nestedEnabled, stack, savedStacks)
        }

        return stack.toList()
    }

    /**
     * Processes a single top-level PSI element, mutating [stack] and
     * [savedStacks] in-place. Returns the (possibly updated) [nestedEnabled]
     * flag.
     */
    private fun processElement(
        element: PsiElement,
        nestedEnabled: Boolean,
        stack: ArrayDeque<String>,
        savedStacks: ArrayDeque<Pair<Boolean, List<String>>>
    ): Boolean {
        var nested = nestedEnabled

        // namespace nested on/off
        val nestedConfig = PsiTreeUtil.findChildOfType(element, AsarNamespaceConfigurationDirective::class.java)
        if (nestedConfig != null) {
            val onOff = nestedConfig.namespaceOnOff
            nested = onOff?.firstChild?.text?.lowercase() == "on"
            if (!nested) {
                val top = stack.lastOrNull()
                stack.clear()
                if (top != null) stack.addLast(top)
            }
            return nested
        }

        // pushns
        if (PsiTreeUtil.findChildOfType(element, AsarPushnsDirective::class.java) != null) {
            savedStacks.addLast(nested to stack.toList())
            return nested
        }

        // pullns
        if (PsiTreeUtil.findChildOfType(element, AsarPullnsDirective::class.java) != null) {
            if (savedStacks.isNotEmpty()) {
                val (savedNested, savedStack) = savedStacks.removeLast()
                nested = savedNested
                stack.clear()
                savedStack.forEach { stack.addLast(it) }
            }
            return nested
        }

        // namespace off
        if (PsiTreeUtil.findChildOfType(element, AsarNamespaceOffDirective::class.java) != null) {
            if (nested) {
                if (stack.isNotEmpty()) stack.removeLast()
            } else {
                stack.clear()
            }
            return nested
        }

        // namespace <name>
        val enter = PsiTreeUtil.findChildOfType(element, AsarNamespaceEnterDirective::class.java)
        if (enter != null) {
            val name = enter.node.getChildren(NAMESPACE_NAME_TOKENS).firstOrNull()?.text ?: return nested
            if (nested) {
                stack.addLast(name)
            } else {
                stack.clear()
                stack.addLast(name)
            }
            return nested
        }

        return nested
    }
}
