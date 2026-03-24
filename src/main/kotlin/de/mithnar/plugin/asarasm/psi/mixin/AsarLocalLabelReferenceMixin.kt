package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import de.mithnar.plugin.asarasm.psi.AsarLocalLabelReference
import de.mithnar.plugin.asarasm.reference.AsarLocalLabelPsiReference

abstract class AsarLocalLabelReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node), AsarLocalLabelReference {

    override fun getReference(): PsiReference =
        AsarLocalLabelPsiReference(this)
}