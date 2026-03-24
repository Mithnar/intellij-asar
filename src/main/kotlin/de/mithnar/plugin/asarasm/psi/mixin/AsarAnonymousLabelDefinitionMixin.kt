package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

abstract class AsarAnonymousLabelDefinitionMixin(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {

    // The "name" is the full text — "-", "--", "+", "++" etc.
    override fun getName(): String = text

    override fun getNameIdentifier(): PsiElement = this

    override fun setName(name: String): PsiElement {
        throw UnsupportedOperationException("Anonymous labels cannot be renamed")
    }
}