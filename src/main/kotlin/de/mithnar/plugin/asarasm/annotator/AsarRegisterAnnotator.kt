package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import de.mithnar.plugin.asarasm.AsarSyntaxHighlighter
import de.mithnar.plugin.asarasm.psi.*
import de.mithnar.plugin.asarasm.psi.AsarTypes

/**
 * Highlights register and built-in tokens that are now lexed as IDENTIFIER_TOKEN:
 *   a, x, y, s -> in indexRegister, accumulatorOperand, stackOperand, stackRelativeIndirectIndexedMode
 *   pc -> in primaryExpression
 *
 * Expected structures:
 *
 * pc in expression:
 *   AsarPcExpressionImpl(PC_EXPRESSION)
 *     PsiElement(IDENTIFIER_TOKEN)('pc') <- matching element, parent is LABEL_REFERENCE
 *
 * a as accumulator:
 *   AsarAccumulatorOperandImpl(ACCUMULATOR_OPERAND)
 *     PsiElement(IDENTIFIER_TOKEN)('a') <- matching element, parent is ACCUMULATOR_OPERAND
 *
 * x/y/s as index register:
 *   AsarIndexRegisterImpl(INDEX_REGISTER)
 *     PsiElement(IDENTIFIER_TOKEN)('x') <- matching element, parent is INDEX_REGISTER
 *
 * s as stack operand:
 *   AsarStackOperandImpl(STACK_OPERAND)
 *     PsiElement(IDENTIFIER_TOKEN)('s') <- matching element, parent is STACK_OPERAND
 */
class AsarRegisterAnnotator : Annotator {

    private val registerNames = setOf("a", "x", "y", "s", "pc")

    override fun annotate(el: PsiElement, holder: AnnotationHolder) {
        // Needs to be an identifier and one of the tokens in registerNames
        if (el.node.elementType != AsarTypes.IDENTIFIER_TOKEN) return
        if (!el.text.lowercase().let { it in registerNames }) return

        val parent = el.parent ?: return
        val isRegisterContext = when {
            // pc as pc expression
            parent is AsarPcExpression -> true
            // a as accumulator operand
            parent.node.elementType == AsarTypes.ACCUMULATOR_OPERAND -> true
            // x, y, s as index register
            parent is AsarIndexRegister -> true
            // s as stack operand
            parent.node.elementType == AsarTypes.STACK_OPERAND -> true
            else -> false
        }
        if (!isRegisterContext) return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(el.textRange)
            .textAttributes(AsarSyntaxHighlighter.REGISTER)
            .create()
    }
}