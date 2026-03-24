package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import de.mithnar.plugin.asarasm.psi.AsarTypes

abstract class AsarLabelDefinitionMixin(node: ASTNode) : ASTWrapperPsiElement(node), PsiNamedElement {

    // Remove the colon
    override fun getName(): String? {
        val token = node.findChildByType(AsarTypes.LABEL_TOKEN) ?: return null
        return token.text.trimEnd(':')
    }

    override fun setName(name: String): PsiElement {
        throw UnsupportedOperationException("Rename not yet supported")
    }
}