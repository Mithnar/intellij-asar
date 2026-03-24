package de.mithnar.plugin.asarasm

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import de.mithnar.plugin.asarasm.psi.AsarTypes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class AsarLexerTokenTypeTest {

    private fun tokenize(text: String): List<Pair<IElementType, String>> {
        val lexer = AsarLexerAdapter()
        lexer.start(text)
        val tokens = mutableListOf<Pair<IElementType, String>>()
        while (lexer.tokenType != null) {
            tokens.add(lexer.tokenType!! to lexer.tokenText)
            lexer.advance()
        }
        return tokens
    }

    @ParameterizedTest(name = "Label: {0}")
    @MethodSource("labels")
    fun testLabelTokenTypes(
        @Suppress("UNUSED_PARAMETER") name: String,
        input: String,
        expected: List<Pair<IElementType, String>>
    ) {
        val actual = tokenize(input)
        Assertions.assertEquals(expected, actual, "Token mismatch for: $input")
    }

    @ParameterizedTest(name = "Directive: {0}")
    @MethodSource("directives")
    fun testDirectiveTokenTypes(
        @Suppress("UNUSED_PARAMETER") name: String,
        input: String,
        expected: List<Pair<IElementType, String>>
    ) {
        val actual = tokenize(input)
        Assertions.assertEquals(expected, actual, "Token mismatch for: $input")
    }

    @ParameterizedTest(name = "Comment: {0}")
    @MethodSource("comments")
    fun testCommentTokenTypes(
        @Suppress("UNUSED_PARAMETER") name: String,
        input: String,
        expected: List<Pair<IElementType, String>>
    ) {
        val actual = tokenize(input)
        Assertions.assertEquals(expected, actual, "Token mismatch for: $input")
    }

    @ParameterizedTest(name = "Special: {0}")
    @MethodSource("specialKeywords")
    fun testSpecialKeywordTokenTypes(
        @Suppress("UNUSED_PARAMETER") name: String,
        input: String,
        expected: List<Pair<IElementType, String>>
    ) {
        val actual = tokenize(input)
        Assertions.assertEquals(expected, actual, "Token mismatch for: $input")
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("addressingModes")
    fun testTokenTypes(
        @Suppress("UNUSED_PARAMETER") name: String,
        input: String,
        expected: List<Pair<IElementType, String>>
    ) {
        val actual = tokenize(input)
        Assertions.assertEquals(expected, actual, "Token mismatch for: $input")
    }

    companion object {
        private val COMMENT = AsarTypes.COMMENT_TOKEN
        private val OPCODE = AsarTypes.OPCODE_TOKEN
        private val IMPLIED_OPCODE = AsarTypes.IMPLIED_OPCODE_TOKEN
        private val BLOCK_MOVE_OPCODE = AsarTypes.BLOCK_MOVE_OPCODE_TOKEN
        private val IDENTIFIER = AsarTypes.IDENTIFIER_TOKEN
        private val BRANCH = AsarTypes.BRANCH_OPCODE_TOKEN
        private val JUMP = AsarTypes.JUMP_OPCODE_TOKEN
        private val LABEL = AsarTypes.LABEL_TOKEN
        private val STRING = AsarTypes.STRING_TOKEN
        private val CONSTANT = AsarTypes.CONSTANT_TOKEN
        private val NUMBER = AsarTypes.NUMBER_TOKEN
        private val PLUS = AsarTypes.PLUS_TOKEN;
        private val MINUS = AsarTypes.MINUS_TOKEN;
        private val HASH = AsarTypes.HASH_TOKEN
        private val COMMA = AsarTypes.COMMA_TOKEN
        private val XREG = AsarTypes.X_TOKEN
        private val YREG = AsarTypes.Y_TOKEN
        private val AREG = AsarTypes.A_TOKEN
        private val PC = AsarTypes.PC_TOKEN
        private val LPAREN = AsarTypes.LPAREN_TOKEN
        private val RPAREN = AsarTypes.RPAREN_TOKEN
        private val LBRACKET = AsarTypes.LBRACKET_TOKEN
        private val RBRACKET = AsarTypes.RBRACKET_TOKEN
        private val STACK = AsarTypes.STACK_TOKEN
        private val WS = TokenType.WHITE_SPACE
        private val NEWLINE = AsarTypes.NEWLINE_TOKEN

        @JvmStatic
        fun labels(): List<Array<Any>> = listOf(
            arrayOf(
                "Label definition", "Main:", listOf(
                    LABEL to "Main:"
                )
            ),
            arrayOf(
                "Label and instruction", "Loop:\n  LDA $00", listOf(
                    LABEL to "Loop:", NEWLINE to "\n", WS to "  ", OPCODE to "LDA", WS to " ", NUMBER to "$00"
                )
            ),
            arrayOf(
                "Constant usage", "LDA !MY_VAR", listOf(
                    OPCODE to "LDA", WS to " ", CONSTANT to "!MY_VAR"
                )
            ),
            arrayOf(
                "Anonymous label forward", "BNE +", listOf(
                    BRANCH to "BNE", WS to " ", PLUS to "+"
                )
            ),
            arrayOf(
                "Anonymous label backward", "BNE -", listOf(
                    BRANCH to "BNE", WS to " ", MINUS to "-"
                )
            ),
            arrayOf(
                "label before db", "SPRITE_GRAVITY: db \$04,\$04", listOf(
                    LABEL to "SPRITE_GRAVITY:",
                    WS to " ",
                    IDENTIFIER to "db",
                    WS to " ",
                    NUMBER to "$04",
                    COMMA to ",",
                    NUMBER to "$04"
                )
            )
        )

        @JvmStatic
        fun directives(): List<Array<Any>> = listOf(
            arrayOf(
                "org directive", "org $008000", listOf(
                    IDENTIFIER to "org", WS to " ", NUMBER to "$008000"
                )
            ),
            arrayOf(
                "db single", "db $00", listOf(
                    IDENTIFIER to "db", WS to " ", NUMBER to "$00"
                )
            ),
            arrayOf(
                "db multiple", "db $00,$01,$02", listOf(
                    IDENTIFIER to "db",
                    WS to " ",
                    NUMBER to "$00",
                    COMMA to ",",
                    NUMBER to "$01",
                    COMMA to ",",
                    NUMBER to "$02"
                )
            ),
            arrayOf(
                "dw", "dw $1234", listOf(
                    IDENTIFIER to "dw", WS to " ", NUMBER to "$1234"
                )
            ),
            arrayOf(
                "dl", "dl $123456", listOf(
                    IDENTIFIER to "dl", WS to " ", NUMBER to "$123456"
                )
            ),
            arrayOf(
                "incbin", "incbin \"file.bin\"", listOf(
                    IDENTIFIER to "incbin", WS to " ", STRING to "\"file.bin\""
                )
            ),
            arrayOf(
                "print", "print \"hello\"", listOf(
                    IDENTIFIER to "print", WS to " ", STRING to "\"hello\""
                )
            ),
            arrayOf(
                "print with string and pc", "print \"MAIN \",pc", listOf(
                    IDENTIFIER to "print",
                    WS to " ",
                    STRING to "\"MAIN \"",
                    COMMA to ",",
                    PC to "pc"
                )
            ),
        )

        @JvmStatic
        fun comments(): List<Array<Any>> = listOf(
            arrayOf(
                "Full line comment", "; this is a comment", listOf(
                    COMMENT to "; this is a comment"
                )
            ),
            arrayOf(
                "Inline comment", "NOP ; do nothing", listOf(
                    IMPLIED_OPCODE to "NOP", WS to " ", COMMENT to "; do nothing"
                )
            )
        )

        @JvmStatic
        fun specialKeywords(): List<Array<Any>> = listOf(
            arrayOf(
                "pc keyword", "org pc", listOf(
                    IDENTIFIER to "org", WS to " ", PC to "pc"
                )
            )
        )

        @JvmStatic
        fun addressingModes(): List<Array<Any>> = listOf(
            arrayOf(
                "Implied", "NOP", listOf(
                    IMPLIED_OPCODE to "NOP"
                )
            ),
            arrayOf(
                "Accumulator", "ASL A", listOf(
                    OPCODE to "ASL", WS to " ", AREG to "A"
                )
            ),
            arrayOf(
                "Immediate 8-bit", "LDA #$12", listOf(
                    OPCODE to "LDA", WS to " ", HASH to "#", NUMBER to "$12"
                )
            ),
            arrayOf(
                "Immediate 16-bit", "LDA #$1234", listOf(
                    OPCODE to "LDA", WS to " ", HASH to "#", NUMBER to "$1234"
                )
            ),
            arrayOf(
                "Direct Page", "LDA $12", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$12"
                )
            ),
            arrayOf(
                "DP,X", "LDA $12,x", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$12", COMMA to ",", XREG to "x"
                )
            ),
            arrayOf(
                "DP,Y", "LDX $12,y", listOf(
                    OPCODE to "LDX", WS to " ", NUMBER to "$12", COMMA to ",", YREG to "y"
                )
            ),
            arrayOf(
                "Absolute", "LDA $1234", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$1234"
                )
            ),
            arrayOf(
                "Absolute,X", "LDA $1234,x", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$1234", COMMA to ",", XREG to "x"
                )
            ),
            arrayOf(
                "Absolute,Y", "LDA $1234,y", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$1234", COMMA to ",", YREG to "y"
                )
            ),
            arrayOf(
                "Long", "LDA $123456", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$123456"
                )
            ),
            arrayOf(
                "Long,X", "LDA $123456,x", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$123456", COMMA to ",", XREG to "x"
                )
            ),
            arrayOf(
                "(DP)", "LDA ($12)", listOf(
                    OPCODE to "LDA", WS to " ", LPAREN to "(", NUMBER to "$12", RPAREN to ")"
                )
            ),
            arrayOf(
                "(DP),Y", "LDA ($12),y", listOf(
                    OPCODE to "LDA",
                    WS to " ",
                    LPAREN to "(",
                    NUMBER to "$12",
                    RPAREN to ")",
                    COMMA to ",",
                    YREG to "y"
                )
            ),
            arrayOf(
                "(DP,X)", "LDA ($12,x)", listOf(
                    OPCODE to "LDA", WS to " ", LPAREN to "(", NUMBER to "$12", COMMA to ",", XREG to "x", RPAREN to ")"
                )
            ),
            arrayOf(
                "[DP]", "LDA [$12]", listOf(
                    OPCODE to "LDA", WS to " ", LBRACKET to "[", NUMBER to "$12", RBRACKET to "]"
                )
            ),
            arrayOf(
                "[DP],Y", "LDA [$12],y", listOf(
                    OPCODE to "LDA", WS to " ", LBRACKET to "[", NUMBER to "$12", RBRACKET to "]",
                    COMMA to ",", YREG to "y"
                )
            ),
            arrayOf(
                "SR,S", "LDA $12,s", listOf(
                    OPCODE to "LDA", WS to " ", NUMBER to "$12", COMMA to ",", STACK to "s"
                )
            ),
            arrayOf(
                "(SR,S),Y", "LDA ($12,s),y", listOf(
                    OPCODE to "LDA",
                    WS to " ",
                    LPAREN to "(",
                    NUMBER to "$12",
                    COMMA to ",",
                    STACK to "s",
                    RPAREN to ")",
                    COMMA to ",",
                    YREG to "y"
                )
            ),
            arrayOf(
                "Branch", "BNE $12", listOf(
                    BRANCH to "BNE", WS to " ", NUMBER to "$12"
                )
            ),
            arrayOf(
                "Branch Long", "BRL $1234", listOf(
                    BRANCH to "BRL", WS to " ", NUMBER to "$1234"
                )
            ),
            arrayOf(
                "JMP abs", "JMP $1234", listOf(
                    JUMP to "JMP", WS to " ", NUMBER to "$1234"
                )
            ),
            arrayOf(
                "JMP (abs)", "JMP ($1234)", listOf(
                    JUMP to "JMP", WS to " ", LPAREN to "(", NUMBER to "$1234", RPAREN to ")"
                )
            ),
            arrayOf(
                "JMP (abs,X)", "JMP ($1234,x)", listOf(
                    JUMP to "JMP", WS to " ", LPAREN to "(", NUMBER to "$1234", COMMA to ",", XREG to "x", RPAREN to ")"
                )
            ),
            arrayOf(
                "JML long", "JML $123456", listOf(
                    JUMP to "JML", WS to " ", NUMBER to "$123456"
                )
            ),
            arrayOf(
                "JML [abs]", "JML [$1234]", listOf(
                    JUMP to "JML", WS to " ", LBRACKET to "[", NUMBER to "$1234", RBRACKET to "]"
                )
            ),
            arrayOf(
                "JSR abs", "JSR $1234", listOf(
                    JUMP to "JSR", WS to " ", NUMBER to "$1234"
                )
            ),
            arrayOf(
                "JSL long", "JSL $123456", listOf(
                    JUMP to "JSL", WS to " ", NUMBER to "$123456"
                )
            ),
            arrayOf(
                "MVP", "MVP $12,$34", listOf(
                    BLOCK_MOVE_OPCODE to "MVP", WS to " ", NUMBER to "$12", COMMA to ",", NUMBER to "$34"
                )
            ),
            arrayOf(
                "PEI", "PEI ($12)", listOf(
                    OPCODE to "PEI", WS to " ", LPAREN to "(", NUMBER to "$12", RPAREN to ")"
                )
            )
        )
    }
}