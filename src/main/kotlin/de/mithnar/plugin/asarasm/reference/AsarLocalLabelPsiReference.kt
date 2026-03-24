package de.mithnar.plugin.asarasm.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.AsarLocalLabelDefinition
import de.mithnar.plugin.asarasm.psi.AsarLocalLabelReference

class AsarLocalLabelPsiReference(element: AsarLocalLabelReference) :
    PsiReferenceBase<AsarLocalLabelReference>(element, TextRange(0, element.textLength)) {

    override fun resolve(): PsiElement? {
        val name = element.node.text
        return PsiTreeUtil.findChildrenOfType(element.containingFile, AsarLocalLabelDefinition::class.java)
            .firstOrNull { it.node.text == name }
    }

    override fun getVariants(): Array<Any> = emptyArray()
}