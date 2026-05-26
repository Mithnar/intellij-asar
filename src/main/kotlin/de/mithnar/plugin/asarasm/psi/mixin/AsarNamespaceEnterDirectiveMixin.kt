package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.TokenSet
import de.mithnar.plugin.asarasm.psi.AsarTypes


abstract class AsarNamespaceEnterDirectiveMixin(node: ASTNode) : ASTWrapperPsiElement(node) {
    companion object {
        val NAMESPACE_NAME_TOKENS = TokenSet.create(
            AsarTypes.IDENTIFIER_TOKEN,
            AsarTypes.OPCODE_TOKEN,
            AsarTypes.IMPLIED_OPCODE_TOKEN,
            AsarTypes.BLOCK_MOVE_OPCODE_TOKEN,
            AsarTypes.BRANCH_OPCODE_TOKEN,
            AsarTypes.JUMP_OPCODE_TOKEN,
        )
    }

    fun getNamespaceName(): String? =
        node.getChildren(NAMESPACE_NAME_TOKENS).firstOrNull()?.text
}
