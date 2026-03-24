package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import de.mithnar.plugin.asarasm.AsarSyntaxHighlighter
import de.mithnar.plugin.asarasm.psi.AsarDirective
import de.mithnar.plugin.asarasm.psi.AsarTypes

class AsarDirectiveAnnotator : Annotator {

    /**
     *  Expected structure: (example for print directive)
     *  AsarDirectiveImpl(DIRECTIVE) <- ParentOfParent
     *      AsarPrintDirectiveImpl(PRINT_DIRECTIVE) <- Parent
     *          PsiElement(IDENTIFIER_TOKEN)('print') <- matching element
     *
     *  Grammar rule:
     *  directive ::= printDirective | orgDirective | ...
     *  printDirective ::= <<kw_print>> printArgumentList?
     *
     *  Note:
     *  The lexer can't match directives, because they are not reserved keywords and can be used as other identifiers.
     *  Only the context of the identifier decides if it is a directive.
     */
    override fun annotate(el: PsiElement, holder: AnnotationHolder) {
        // Needs to be an identifier
        if (el.node.elementType != AsarTypes.IDENTIFIER_TOKEN) return

        // ParentOfParent is a directive (parent is a specific directive, so harder to match)
        val parentOfParent = el.parent?.parent ?: return
        if (parentOfParent !is AsarDirective) return

        //  Only the first child is the directive. (print "Hello" -> only print is the directive)
        val firstChild = generateSequence(parentOfParent.firstChild) { it.firstChild }.last()
        if (firstChild != el) return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(AsarSyntaxHighlighter.DIRECTIVE)
            .create()
    }
}