package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import de.mithnar.plugin.asarasm.reference.AsarAnonymousLabelPsiReference

abstract class AsarAnonymousLabelReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        return arrayOf(AsarAnonymousLabelPsiReference(this, textRangeInParent))
    }

    override fun getReference(): PsiReference? = references.firstOrNull()
}