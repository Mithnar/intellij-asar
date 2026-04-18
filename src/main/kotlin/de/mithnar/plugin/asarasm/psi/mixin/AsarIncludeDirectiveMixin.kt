package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.TokenSet
import de.mithnar.plugin.asarasm.psi.AsarTypes
import de.mithnar.plugin.asarasm.reference.AsarIncludePsiReference

abstract class AsarIncludeDirectiveMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        val tokens = node.getChildren(TokenSet.create(AsarTypes.IDENTIFIER_TOKEN, AsarTypes.STRING_TOKEN))
        if (tokens.size != 2) return PsiReference.EMPTY_ARRAY

        val filenameToken = tokens[1]
        val range = filenameToken.textRange.shiftLeft(textRange.startOffset)
        val filenameRange = if (filenameToken.elementType == AsarTypes.STRING_TOKEN) {
            range.cutOut(TextRange(1, filenameToken.text.length - 1))
        } else {
            range
        }

        return arrayOf(AsarIncludePsiReference(this, filenameRange))
    }

    override fun getReference(): PsiReference? = references.firstOrNull()
}
