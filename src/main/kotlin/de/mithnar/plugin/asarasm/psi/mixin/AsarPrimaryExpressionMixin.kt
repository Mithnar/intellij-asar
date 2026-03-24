package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.AsarMacroCall
import de.mithnar.plugin.asarasm.psi.AsarOperand
import de.mithnar.plugin.asarasm.psi.AsarTypes
import de.mithnar.plugin.asarasm.reference.AsarLabelPsiReference

abstract class AsarPrimaryExpressionMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        val token = node.firstChildNode ?: return PsiReference.EMPTY_ARRAY
        if (token.elementType != AsarTypes.IDENTIFIER_TOKEN) return PsiReference.EMPTY_ARRAY

        // Must be inside an operand, not inside a macro call
        PsiTreeUtil.getParentOfType(this, AsarOperand::class.java, false)
            ?: return PsiReference.EMPTY_ARRAY
        val macroCall = PsiTreeUtil.getParentOfType(this, AsarMacroCall::class.java, false)
        if (macroCall != null) return PsiReference.EMPTY_ARRAY

        return arrayOf(AsarLabelPsiReference(this, token.textRange.shiftLeft(textRange.startOffset)))
    }

    override fun getReference(): PsiReference? = getReferences().firstOrNull()
}