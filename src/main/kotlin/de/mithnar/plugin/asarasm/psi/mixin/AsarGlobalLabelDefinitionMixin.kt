package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import de.mithnar.plugin.asarasm.psi.AsarTypes

abstract class AsarGlobalLabelDefinitionMixin(node: ASTNode) : ASTWrapperPsiElement(node), PsiNamedElement {

    override fun getName(): String? {
        node.findChildByType(AsarTypes.LABEL_TOKEN)?.let {
            return it.text.trimEnd(':')
        }
        node.findChildByType(AsarTypes.IDENTIFIER_TOKEN)?.let {
            return it.text
        }
        return null
    }

    override fun setName(name: String): PsiElement =
        throw UnsupportedOperationException("Rename not yet supported")
}
