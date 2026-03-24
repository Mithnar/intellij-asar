package de.mithnar.plugin.asarasm.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.AsarLabelDefinition
import de.mithnar.plugin.asarasm.psi.AsarTypes

class AsarLabelPsiReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    private val referencedName: String
        get() {
            val token = element.node.firstChildNode
            return token?.text ?: element.text
        }

    override fun resolve(): PsiElement? {
        val file = element.containingFile
        val allLabels = PsiTreeUtil.collectElementsOfType(file, AsarLabelDefinition::class.java)

        return allLabels.firstOrNull { labelName(it) == referencedName }
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile

        return PsiTreeUtil.collectElementsOfType(file, AsarLabelDefinition::class.java)
            .mapNotNull { labelName(it) }
            .toTypedArray()
    }

    private fun labelName(label: AsarLabelDefinition): String? {
        val token = label.node.findChildByType(AsarTypes.LABEL_TOKEN) ?: return null

        return token.text.trimEnd(':')
    }
}