package de.mithnar.plugin.asarasm.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.AsarFile
import de.mithnar.plugin.asarasm.psi.AsarGlobalLabelDefinition
import de.mithnar.plugin.asarasm.psi.AsarLabelDefinition

/**
 * Resolves a label reference to its definition, taking the active namespace
 * stack into account.
 *
 * Resolution order for a reference "Main" inside namespace Deep > Deeper:
 *   1. Deep_Deeper_Main
 *   2. Deep_Main
 *   3. Main   (global fallback)
 *
 * Global labels (defined with the `global` keyword) are always resolved
 * against their literal name with no namespace prefix applied.
 */
class AsarLabelPsiReference(
    element: PsiElement,
    range: TextRange
) : PsiReferenceBase<PsiElement>(element, range) {

    private val referencedName: String
        get() = element.text.substring(rangeInElement.startOffset, rangeInElement.endOffset)

    override fun resolve(): PsiElement? {
        val file = element.containingFile as? AsarFile ?: return null
        val name = referencedName

        val candidates = AsarNamespaceResolver.buildCandidateNames(name, element)

        val labelDefs = PsiTreeUtil.findChildrenOfType(file, AsarLabelDefinition::class.java)
        val globalDefs = PsiTreeUtil.findChildrenOfType(file, AsarGlobalLabelDefinition::class.java)

        for (candidate in candidates) {
            globalDefs.firstOrNull { (it as? PsiNamedElement)?.name == candidate }?.let { return it }
            labelDefs.firstOrNull { (it as? PsiNamedElement)?.name == candidate }?.let { return it }
        }

        return null
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile as? AsarFile ?: return emptyArray()

        val labelDefs = PsiTreeUtil.findChildrenOfType(file, AsarLabelDefinition::class.java)
            .mapNotNull { (it as? PsiNamedElement)?.name }
        val globalDefs = PsiTreeUtil.findChildrenOfType(file, AsarGlobalLabelDefinition::class.java)
            .mapNotNull { (it as? PsiNamedElement)?.name }

        return (labelDefs + globalDefs).distinct().toTypedArray()
    }
}