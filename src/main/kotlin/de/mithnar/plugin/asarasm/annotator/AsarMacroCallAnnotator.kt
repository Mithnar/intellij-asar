package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import de.mithnar.plugin.asarasm.AsarSyntaxHighlighter
import de.mithnar.plugin.asarasm.psi.AsarTypes

/**
 * Expected structure:
 * AsarMacroCallImpl(MACRO_CALL)
 *  PsiElement(PERCENT_TOKEN)('%')
 *  PsiElement(IDENTIFIER_TOKEN)('MyMacro') <- matching element
 *  AsarMacroArgumentsImpl
 *      AsarMacroArgumentListImpl
 *          AsarMacroArgumentImpl
 *              AsarExpressionImpl
 *                  ...AsarPrimaryExpressionImpl
 *                      PsiElement(IDENTIFIER_TOKEN)('arg') <- parent is PRIMARY_EXPRESSION, so not matching
 *
 *  Grammar rule:
 *  macroCall ::= PERCENT_TOKEN IDENTIFIER_TOKEN macroArguments?
 *  macroArguments ::= LPAREN_TOKEN macroArgumentList? RPAREN_TOKEN
 *  macroArgumentList ::= macroArgument (COMMA_TOKEN macroArgument)*
 *  macroArgument ::= STRING_TOKEN | expression
 */
class AsarMacroCallAnnotator : Annotator {
    override fun annotate(el: PsiElement, holder: AnnotationHolder) {
        // Needs to be an identifier
        if (el.node.elementType != AsarTypes.IDENTIFIER_TOKEN) return

        // Parent needs to be a macro call, see expected structure
        val parent = el.parent ?: return
        if (parent.node.elementType != AsarTypes.MACRO_CALL) return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(el.textRange)
            .textAttributes(AsarSyntaxHighlighter.MACRO_CALL)
            .create()
    }
}