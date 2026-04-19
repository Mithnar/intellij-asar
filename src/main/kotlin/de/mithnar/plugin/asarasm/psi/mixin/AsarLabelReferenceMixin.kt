package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import de.mithnar.plugin.asarasm.psi.AsarTypes
import de.mithnar.plugin.asarasm.reference.AsarLabelPsiReference

abstract class AsarLabelReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        val token = node.findChildByType(AsarTypes.IDENTIFIER_TOKEN) ?: return PsiReference.EMPTY_ARRAY
        val tokenRange = token.textRange.shiftLeft(textRange.startOffset)
        val tokenText = token.text

        val dotIndex = tokenText.indexOf('.')
        if (dotIndex < 0) {
            // Simple label: "MyLabel"
            return arrayOf(AsarLabelPsiReference(this, tokenRange))
        }

        // Namespaced label: "Namespace.Field"
        val fullyQualifiedRef = AsarLabelPsiReference(this, tokenRange)

        // Reference for just the namespace part
        val namespaceRange = TextRange(tokenRange.startOffset, tokenRange.startOffset + dotIndex)
        val namespaceRef = AsarLabelPsiReference(this, namespaceRange)

        // Reference for just the local/field part (including the dot)
        val localRange = TextRange(tokenRange.startOffset + dotIndex, tokenRange.endOffset)
        val localRef = AsarLabelPsiReference(this, localRange)

        return arrayOf(fullyQualifiedRef, namespaceRef, localRef)
    }

    override fun getReference(): PsiReference? = references.firstOrNull()
}