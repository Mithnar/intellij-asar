package de.mithnar.plugin.asarasm.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.psi.AsarAnonymousLabelDefinition

class AsarAnonymousLabelPsiReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    override fun resolve(): PsiElement? {
        val labelText = element.text
        val isForward = labelText.startsWith("+")
        val file = element.containingFile
        val thisOffset = element.textOffset

        // Now we search only AsarAnonymousLabelDefinition nodes — no role-guessing needed
        val allDefinitions = PsiTreeUtil.collectElementsOfType(
            file, AsarAnonymousLabelDefinition::class.java
        ).filter { it.text == labelText }

        return if (isForward) {
            allDefinitions.filter { it.textOffset > thisOffset }.minByOrNull { it.textOffset }
        } else {
            allDefinitions.filter { it.textOffset < thisOffset }.maxByOrNull { it.textOffset }
        }
    }

    override fun getVariants(): Array<Any> = emptyArray() // No completion for anonymous labels
}