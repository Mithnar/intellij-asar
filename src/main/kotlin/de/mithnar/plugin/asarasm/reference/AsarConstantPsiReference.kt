package de.mithnar.plugin.asarasm.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.AsarConstantDefinition
import de.mithnar.plugin.asarasm.psi.AsarTypes

class AsarConstantPsiReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    private val referencedName: String
        get() = element.node.firstChildNode?.text ?: element.text

    override fun resolve(): PsiElement? {
        val file = element.containingFile
        return PsiTreeUtil.collectElementsOfType(file, AsarConstantDefinition::class.java)
            .firstOrNull { constantName(it) == referencedName }
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile
        return PsiTreeUtil.collectElementsOfType(file, AsarConstantDefinition::class.java)
            .mapNotNull { constantName(it) }
            .toTypedArray()
    }

    // The "name" is the full text including ! -> "!CONST"
    private fun constantName(def: AsarConstantDefinition): String? {
        val token = def.node.findChildByType(AsarTypes.CONSTANT_TOKEN) ?: return null
        return token.text
    }
}