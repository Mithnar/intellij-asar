package de.mithnar.plugin.asarasm

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import de.mithnar.plugin.asarasm.psi.AsarTypes

class AsarSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val OPCODE = createTextAttributesKey("ASAR_OPCODE", DefaultLanguageHighlighterColors.KEYWORD)
        val DIRECTIVE = createTextAttributesKey("ASAR_DIRECTIVE", DefaultLanguageHighlighterColors.METADATA)
        val MACRO_CALL = createTextAttributesKey("ASAR_MACRO_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val COMMENT = createTextAttributesKey("ASAR_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val LABEL = createTextAttributesKey("ASAR_LABEL", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val CONSTANT = createTextAttributesKey("ASAR_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val NUMBER = createTextAttributesKey("ASAR_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val STRING = createTextAttributesKey("ASAR_STRING", DefaultLanguageHighlighterColors.STRING)
        val OPERATOR = createTextAttributesKey("ASAR_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val BRACKETS = createTextAttributesKey("ASAR_PUNCTUATION", DefaultLanguageHighlighterColors.BRACKETS)
        val REGISTER = createTextAttributesKey("ASAR_REGISTER", DefaultLanguageHighlighterColors.PARAMETER)
    }

    override fun getHighlightingLexer(): Lexer = AsarLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        when (tokenType) {
            AsarTypes.OPCODE_TOKEN,
            AsarTypes.IMPLIED_OPCODE_TOKEN,
            AsarTypes.BLOCK_MOVE_OPCODE_TOKEN,
            AsarTypes.BRANCH_OPCODE_TOKEN,
            AsarTypes.JUMP_OPCODE_TOKEN -> pack(OPCODE)

            AsarTypes.COMMENT_TOKEN -> pack(COMMENT)
            AsarTypes.LABEL_TOKEN,
            AsarTypes.LOCAL_LABEL_TOKEN -> pack(LABEL)

            AsarTypes.CONSTANT_TOKEN -> pack(CONSTANT)
            AsarTypes.NUMBER_TOKEN -> pack(NUMBER)
            AsarTypes.STRING_TOKEN -> pack(STRING)

            AsarTypes.MACRO_PARAM_TOKEN -> pack(REGISTER)

            AsarTypes.ASSIGN_TOKEN,
            AsarTypes.EQ_TOKEN,
            AsarTypes.NEQ_TOKEN,
            AsarTypes.LTE_TOKEN,
            AsarTypes.GTE_TOKEN,
            AsarTypes.PLUS_TOKEN,
            AsarTypes.MINUS_TOKEN,
            AsarTypes.STAR_TOKEN,
            AsarTypes.SLASH_TOKEN,
            AsarTypes.PERCENT_TOKEN,
            AsarTypes.PIPE_TOKEN,
            AsarTypes.AMPERSAND_TOKEN,
            AsarTypes.CARET_TOKEN,
            AsarTypes.TILDE_TOKEN,
            AsarTypes.SHIFT_LEFT_TOKEN,
            AsarTypes.SHIFT_RIGHT_TOKEN,
            AsarTypes.HASH_ASSIGN_TOKEN -> pack(OPERATOR)

            AsarTypes.COLON_TOKEN,
            AsarTypes.COMMA_TOKEN,
            AsarTypes.HASH_TOKEN,
            AsarTypes.LPAREN_TOKEN,
            AsarTypes.RPAREN_TOKEN,
            AsarTypes.LBRACKET_TOKEN,
            AsarTypes.RBRACKET_TOKEN -> pack(BRACKETS)

            else -> emptyArray()
        }
}