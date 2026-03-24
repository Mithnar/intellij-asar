package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

abstract class AsarLocalLabelDefinitionMixin(node: ASTNode) : ASTWrapperPsiElement(node), PsiNamedElement {

    override fun getName(): String =
        node.text.trimStart('.').trimEnd(':')

    override fun setName(name: String): PsiElement {
        throw UnsupportedOperationException("Rename not yet supported")
    }
}