package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import de.mithnar.plugin.asarasm.annotator.OpcodeAddressingModes.ALLOWED_MODES
import de.mithnar.plugin.asarasm.extensions.addressingMode
import de.mithnar.plugin.asarasm.psi.AsarGenericInstruction

/**
 * Expected structure:
 * AsarGenericInstructionImpl(GENERIC_INSTRUCTION) <- matching element
 *   PsiElement(OPCODE_TOKEN)('lda')
 *   AsarOperandListImpl(OPERAND_LIST)
 *     AsarOperandImpl(OPERAND)
 *       AsarHashOperandImpl(HASH_OPERAND) <- immediate
 *       AsarIndexedOperandImpl(INDEXED_OPERAND) <- expr,X / expr,Y / expr,S
 *       ... <- etc
 *
 * Grammar rules:
 * genericInstruction ::= OPCODE_TOKEN operandList?
 * operandList        ::= operand (COMMA_TOKEN operand)*
 * operand            ::= hashOperand | indexedIndirectOperand | ... | expression
 */
class AsarAddressingModeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Only applies to generic instructions
        if (element !is AsarGenericInstruction) return

        // Normalize opcode: remove width-suffix
        val opcodeText = element.firstChild.text.lowercase().substringBefore('.')

        // Validate each operand against the allowed adressing modes
        val operands = element.operandList?.operandList ?: emptyList()
        val allowed = ALLOWED_MODES[opcodeText] ?: return
        for (operand in operands) {
            val mode = operand.addressingMode(opcodeText)
            if (mode !in allowed) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Addressing mode '${mode.displayName}' is not valid for '$opcodeText'"
                )
                    .range(operand)
                    .create()
            }
        }
    }
}