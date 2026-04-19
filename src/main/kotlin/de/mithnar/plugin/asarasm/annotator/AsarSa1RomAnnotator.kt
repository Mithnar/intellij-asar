package de.mithnar.plugin.asarasm.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import de.mithnar.plugin.asarasm.psi.AsarRomDirective
import de.mithnar.plugin.asarasm.psi.AsarTypes

/**
 *  Expected structure:
 *  AsarRomDirectiveImpl(ROM_DIRECTIVE)
 *      PsiElement(IDENTIFIER_TOKEN)('sa1rom')
 *      PsiElement(NUMBER_DEC_TOKEN)('0')
 *      PsiElement(COMMA_TOKEN)(',')
 *      PsiElement(NUMBER_DEC_TOKEN)('0')
 *      PsiElement(COMMA_TOKEN)(',')
 *      PsiElement(NUMBER_DEC_TOKEN)('0')
 *      PsiElement(COMMA_TOKEN)(',')
 *      PsiElement(NUMBER_DEC_TOKEN)('0')
 *
 *  Grammar rule:
 *  romDirective ::= ... | <<kw_sa1rom>> sa1romArgs? | ...
 *  sa1romArgs   ::= NUMBER_DEC_TOKEN COMMA_TOKEN NUMBER_DEC_TOKEN COMMA_TOKEN NUMBER_DEC_TOKEN COMMA_TOKEN NUMBER_DEC_TOKEN
 *
 *  Note:
 *  The grammar ensures that sa1rom arguments are decimal number tokens,
 *  but cannot enforce the valid range (0-7). This annotator provides the
 *  semantic validation that each bank value is within 0-7.
 */
class AsarSa1RomAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Needs to be AsarRomDirective
        if (element !is AsarRomDirective) return

        // Only sa1rom supports those parameters
        val keyword = element.firstChild ?: return
        if (!keyword.text.equals("sa1rom", ignoreCase = true)) return

        // Filter out the 4 Decimal Numbers
        val numberTokens = element.node.getChildren(
            TokenSet.create(AsarTypes.NUMBER_DEC_TOKEN)
        )

        // Iterate and check that those are between 0 and 7
        for (numberNode in numberTokens) {
            val value = numberNode.text.toIntOrNull()
            if (value == null || value !in 0..7) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Bank values for sa1rom must be between 0 and 7")
                    .range(numberNode)
                    .create()
            }
        }
    }
}