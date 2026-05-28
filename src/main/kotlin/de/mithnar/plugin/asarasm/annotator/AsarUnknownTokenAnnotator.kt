package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import de.mithnar.plugin.asarasm.psi.AsarUnknownToken

class AsarUnknownTokenAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is AsarUnknownToken) return

        val text = element.text
        val message = when {
            text == "?" -> "Stray '?': macro labels must have no whitespace before the suffix"
            text == "#" -> "Stray '#': use '#expression' for immediate addressing inside an instruction"
            else        -> "Unexpected token '$text'"
        }

        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }
}