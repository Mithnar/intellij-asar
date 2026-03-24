package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import de.mithnar.plugin.asarasm.psi.AsarTypes
import de.mithnar.plugin.asarasm.reference.AsarLabelPsiReference

abstract class AsarLabelReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        val token = node.findChildByType(AsarTypes.IDENTIFIER_TOKEN) ?: return PsiReference.EMPTY_ARRAY
        return arrayOf(AsarLabelPsiReference(this, token.textRange.shiftLeft(textRange.startOffset)))
    }

    override fun getReference(): PsiReference? = references.firstOrNull()
}