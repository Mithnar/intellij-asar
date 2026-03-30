package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.AsarSyntaxHighlighter
import de.mithnar.plugin.asarasm.psi.*

/**
 *  Expected structures:
 *  1. isLabel:
 *  AsarLabelDefinitionImpl(LABEL_DEFINITION) ← matching element
 *  AsarLabelReferenceImpl(LABEL_REFERENCE) ← matching element
 *
 *  Grammar rule:
 *  labelDefinition ::= LABEL_TOKEN
 *
 *  2. isLocalLabel:
 *  AsarLocalLabelDefinitionImpl(LOCAL_LABEL_DEFINITION) ← matching element
 *  AsarLocalLabelReferenceImpl(LOCAL_LABEL_REFERENCE) ← matching element
 *
 *  Grammar rule:
 *  labelDefinition ::= LABEL_TOKEN
 *
 *  3. isAnonymousLabelDefinition / isAnonymousLabelReference:
 *  AsarAnonymousLabelDefinitionImpl(ANONYMOUS_LABEL_DEFINITION) ← matching element
 *  AsarAnonymousLabelReferenceImpl(ANONYMOUS_LABEL_REFERENCE) ← matching element
 *
 *  Grammar rule:
 *  anonymousLabelDefinition ::= plusLabel | minusLabel
 *  anonymousLabelReference ::= plusLabel | minusLabel
 *
 *  4. isSymbolReferenceTarget:
 *  AsarSymbolReferenceTargetImpl(SYMBOL_REFERENCE_TARGET) ← matching element
 *
 *  Grammar rule:
 *  branchTarget ::= symbolReferenceTarget | anonymousLabelReference
 *  jumpTarget ::= symbolReferenceTarget | expressionTarget
 *  symbolReferenceTarget ::= IDENTIFIER_TOKEN | CONSTANT_TOKEN
 *
 *  5. isLabelReferenceInOperand
 *  AsarPrimaryExpressionImpl
 *      PsiElement(IDENTIFIER_TOKEN)('MyTable') <- matching element
 *
 *  Grammar rule:
 *  primaryExpression ::= NUMBER_TOKEN
 *                     | STRING_TOKEN
 *                     | MACRO_PARAM_TOKEN
 *                     | localLabelReference
 *                     | functionCall
 *                     | pcExpression
 *                     | labelReference
 *                     | constantReference
 *                     | LPAREN_TOKEN expression RPAREN_TOKEN
 */
class AsarLabelAnnotator : Annotator {

    override fun annotate(el: PsiElement, holder: AnnotationHolder) {
        val isLabel = el is AsarLabelDefinition || el is AsarLabelReference
        val isLocalLabel = el is AsarLocalLabelDefinition || el is AsarLocalLabelReference
        val isAnonymousLabel = el is AsarAnonymousLabelDefinition || el is AsarAnonymousLabelReference
        val isSymbolReferenceTarget = el is AsarSymbolReferenceTarget

        if (isLabel || isLocalLabel || isAnonymousLabel || isSymbolReferenceTarget || isLabelReferenceInOperand(el)) {
            return holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(el.textRange)
                .textAttributes(AsarSyntaxHighlighter.LABEL)
                .create()
        }
    }

    // Case 5
    private fun isLabelReferenceInOperand(el: PsiElement): Boolean {
        // Needs to be an identifier
        if (el.node.elementType != AsarTypes.IDENTIFIER_TOKEN) return false

        // Needs to be part of a primary expression
        PsiTreeUtil.getParentOfType(el, AsarPrimaryExpression::class.java, false) ?: return false

        // Needs to be inside an Operand
        val operand = PsiTreeUtil.getParentOfType(el, AsarOperand::class.java, false)
        return operand != null
    }
}