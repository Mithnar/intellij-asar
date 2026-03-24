package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import de.mithnar.plugin.asarasm.psi.AsarTypes

abstract class AsarConstantDefinitionMixin(node: ASTNode) : ASTWrapperPsiElement(node), PsiNamedElement {

    // The "name" is the full text including ! -> "!CONST"
    override fun getName(): String? {
        val token = node.findChildByType(AsarTypes.CONSTANT_TOKEN) ?: return null
        return token.text
    }

    override fun setName(name: String): PsiElement {
        throw UnsupportedOperationException("Rename not yet supported")
    }
}