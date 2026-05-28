
package de.mithnar.plugin.asarasm.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import de.mithnar.plugin.asarasm.psi.AsarTypes
import de.mithnar.plugin.asarasm.reference.AsarIncludePsiReference

abstract class AsarIncludeDirectiveMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        val filePathNode = node.findChildByType(AsarTypes.FILE_PATH) ?: return PsiReference.EMPTY_ARRAY
        val directiveStart = textRange.startOffset

        val filePathRange = filePathNode.textRange.shiftLeft(directiveStart)

        // If the path is a STRING_TOKEN, strip surrounding quotes.
        val firstChild = filePathNode.firstChildNode
        val filenameRange = if (
            firstChild != null
            && firstChild === filePathNode.lastChildNode
            && firstChild.elementType == AsarTypes.STRING_TOKEN
        ) {
            filePathRange.cutOut(TextRange(1, firstChild.textLength - 1))
        } else {
            filePathRange
        }

        return arrayOf(AsarIncludePsiReference(this, filenameRange))
    }

    override fun getReference(): PsiReference? = references.firstOrNull()
}