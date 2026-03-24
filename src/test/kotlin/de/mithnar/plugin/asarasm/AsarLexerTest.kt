package de.mithnar.plugin.asarasm

import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase

class AsarLexerTest : LexerTestCase() {

    override fun createLexer(): Lexer = AsarLexerAdapter()

    override fun getDirPath(): String = ""

    // Basic

    fun testSimpleOpcode() {
        val given = "LDA $00"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('$00')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testBranchWithLabel() {
        val given = "BNE MyLabel"
        val expected = """
            BRANCH_OPCODE_TOKEN ('BNE')
            WHITE_SPACE (' ')
            IDENTIFIER_TOKEN ('MyLabel')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testLabelDefinition() {
        val given = "Main:"
        val expected = """
            LABEL_TOKEN ('Main:')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testImmediateAddressing() {
        val given = "LDA #$0F"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            HASH_TOKEN ('#')
            NUMBER_TOKEN ('$0F')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testConstant() {
        val given = "LDA !START_POS"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            CONSTANT_TOKEN ('!START_POS')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testComment() {
        val given = "; hello comment"
        val expected = """
            COMMENT_TOKEN ('; hello comment')
            """.trimIndent()
        doTest(given, expected)
    }

    // Directives

    fun testPushPullPc() {
        val given = "pushpc\npullpc"
        val expected = """
            IDENTIFIER_TOKEN ('pushpc')
            NEWLINE_TOKEN ('\n')
            IDENTIFIER_TOKEN ('pullpc')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testBaseDirective() {
        val given = "base $008000"
        val expected = """
            IDENTIFIER_TOKEN ('base')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('$008000')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testBaseOff() {
        val given = "base off"
        val expected = """
            IDENTIFIER_TOKEN ('base')
            WHITE_SPACE (' ')
            IDENTIFIER_TOKEN ('off')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testIfElseEndif() {
        val given = "if !flag\nelse\nendif"
        val expected = """
            IDENTIFIER_TOKEN ('if')
            WHITE_SPACE (' ')
            CONSTANT_TOKEN ('!flag')
            NEWLINE_TOKEN ('\n')
            IDENTIFIER_TOKEN ('else')
            NEWLINE_TOKEN ('\n')
            IDENTIFIER_TOKEN ('endif')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testFillVsFillbyte() {
        val given = "fillbyte ${'$'}00"
        val expected = """
            IDENTIFIER_TOKEN ('fillbyte')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('${'$'}00')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testPadVsPadbyte() {
        val given = "padbyte ${'$'}FF"
        val expected = """
            IDENTIFIER_TOKEN ('padbyte')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('${'$'}FF')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testWhileLoop() {
        val given = "while !counter > 0\nendwhile"
        val expected = """
            IDENTIFIER_TOKEN ('while')
            WHITE_SPACE (' ')
            CONSTANT_TOKEN ('!counter')
            WHITE_SPACE (' ')
            GT_TOKEN ('>')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('0')
            NEWLINE_TOKEN ('\n')
            IDENTIFIER_TOKEN ('endwhile')
        """.trimIndent()
        doTest(given, expected)
    }

    fun testTableAsIdentifier() {
        val given = "STA table,Y"
        val expected = """
            OPCODE_TOKEN ('STA')
            WHITE_SPACE (' ')
            IDENTIFIER_TOKEN ('table')
            COMMA_TOKEN (',')
            Y_TOKEN ('Y')
        """.trimIndent()
        doTest(given, expected)
    }

    // Keyword boundary / EOF fallback edge cases

    fun testOpcodeAtEof() {
        val given = "NOP"
        val expected = """
            IMPLIED_OPCODE_TOKEN ('NOP')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testBranchOpcodeAtEof() {
        val given = "BRA"
        val expected = """
            BRANCH_OPCODE_TOKEN ('BRA')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testJumpOpcodeAtEof() {
        val given = "JMP"
        val expected = """
            JUMP_OPCODE_TOKEN ('JMP')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testDirectiveAtEof() {
        val given = "org"
        val expected = """
            IDENTIFIER_TOKEN ('org')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testOpcodeAsIdentifierPrefix() {
        val given = "LDA_TABLE"
        val expected = """
            IDENTIFIER_TOKEN ('LDA_TABLE')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testBranchOpcodeAsIdentifierPrefix() {
        val given = "BNEflag"
        val expected = """
            IDENTIFIER_TOKEN ('BNEflag')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testDirectiveAsIdentifierPrefix() {
        val given = "organic"
        val expected = """
            IDENTIFIER_TOKEN ('organic')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testDataDirectiveWithData() {
        val given = "db ${'$'}FF,$00"
        val expected = """
            IDENTIFIER_TOKEN ('db')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('${'$'}FF')
            COMMA_TOKEN (',')
            NUMBER_TOKEN ('$00')
            """.trimIndent()
        doTest(given, expected)
    }

    // Case insensitivity

    fun testCaseInsensitiveOpcode() {
        val given = "lda $00"
        val expected = """
            OPCODE_TOKEN ('lda')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('$00')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testMixedCaseOpcode() {
        val given = "Lda $00"
        val expected = """
            OPCODE_TOKEN ('Lda')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('$00')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testCaseInsensitiveRegisters() {
        val given = "X Y A"
        val expected = """
            X_TOKEN ('X')
            WHITE_SPACE (' ')
            Y_TOKEN ('Y')
            WHITE_SPACE (' ')
            A_TOKEN ('A')
            """.trimIndent()
        doTest(given, expected)
    }

    // Single-character ambiguity: registers vs identifiers

    fun testPcKeyword() {
        val given = "LDA pc"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            PC_TOKEN ('pc')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testStackRegister() {
        val given = "s"
        val expected = """
            STACK_TOKEN ('s')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testIdentifierStartingWithS() {
        val given = "sprite_table"
        val expected = """
            IDENTIFIER_TOKEN ('sprite_table')
            """.trimIndent()
        doTest(given, expected)
    }

    // Anonymous labels

    fun testAnonymousForwardLabel() {
        val given = "BRA ++"
        val expected = """
            BRANCH_OPCODE_TOKEN ('BRA')
            WHITE_SPACE (' ')
            PLUS_TOKEN ('+')
            PLUS_TOKEN ('+')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testAnonymousBackwardLabel() {
        val given = "BRA --"
        val expected = """
            BRANCH_OPCODE_TOKEN ('BRA')
            WHITE_SPACE (' ')
            MINUS_TOKEN ('-')
            MINUS_TOKEN ('-')
            """.trimIndent()
        doTest(given, expected)
    }

    // Addressing mode punctuation

    fun testIndirectIndexedTokens() {
        val given = "LDA ($10),Y"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            LPAREN_TOKEN ('(')
            NUMBER_TOKEN ('$10')
            RPAREN_TOKEN (')')
            COMMA_TOKEN (',')
            Y_TOKEN ('Y')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testLongIndirectIndexedTokens() {
        val given = "LDA [$10],Y"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            LBRACKET_TOKEN ('[')
            NUMBER_TOKEN ('$10')
            RBRACKET_TOKEN (']')
            COMMA_TOKEN (',')
            Y_TOKEN ('Y')
            """.trimIndent()
        doTest(given, expected)
    }

    // Expressions with operators

    fun testPrefixOperators() {
        val given = "LDA #<~${'$'}FF"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            HASH_TOKEN ('#')
            LT_TOKEN ('<')
            TILDE_TOKEN ('~')
            NUMBER_TOKEN ('${'$'}FF')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testComplexExpression() {
        val given = "LDA #$7E0000+!offset*2"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            HASH_TOKEN ('#')
            NUMBER_TOKEN ('$7E0000')
            PLUS_TOKEN ('+')
            CONSTANT_TOKEN ('!offset')
            STAR_TOKEN ('*')
            NUMBER_TOKEN ('2')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testBitwiseOperators() {
        val given = "LDA #${'$'}FF&$0F|$80^$01"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            HASH_TOKEN ('#')
            NUMBER_TOKEN ('${'$'}FF')
            AMPERSAND_TOKEN ('&')
            NUMBER_TOKEN ('$0F')
            PIPE_TOKEN ('|')
            NUMBER_TOKEN ('$80')
            CARET_TOKEN ('^')
            NUMBER_TOKEN ('$01')
            """.trimIndent()
        doTest(given, expected)
    }

    // Strings

    fun testStringWithEscapes() {
        val given = """
            "hello \"world\""
        """.trimIndent()
        val expected = """
            STRING_TOKEN ('"hello \"world\""')
        """.trimIndent()
        doTest(given, expected)
    }

    // Macro call

    fun testMacroCallTokens() {
        val given = "%MyMacro()"
        val expected = """
            PERCENT_TOKEN ('%')
            IDENTIFIER_TOKEN ('MyMacro')
            LPAREN_TOKEN ('(')
            RPAREN_TOKEN (')')
            """.trimIndent()
        doTest(given, expected)
    }

    // Constant definition

    fun testConstantDefinition() {
        val given = "!myConst = $0F"
        val expected = """
            CONSTANT_TOKEN ('!myConst')
            WHITE_SPACE (' ')
            ASSIGN_TOKEN ('=')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('$0F')
            """.trimIndent()
        doTest(given, expected)
    }

    // Multiline / colon separator

    fun testColonSeparatedInstructions() {
        val given = "LDA #$00 : STA $10"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            HASH_TOKEN ('#')
            NUMBER_TOKEN ('$00')
            WHITE_SPACE (' ')
            COLON_TOKEN (':')
            WHITE_SPACE (' ')
            OPCODE_TOKEN ('STA')
            WHITE_SPACE (' ')
            NUMBER_TOKEN ('$10')
            """.trimIndent()
        doTest(given, expected)
    }

    fun testNewlineIsNewlineToken() {
        val given = "NOP\nNOP"
        val expected = """
            IMPLIED_OPCODE_TOKEN ('NOP')
            NEWLINE_TOKEN ('\n')
            IMPLIED_OPCODE_TOKEN ('NOP')
        """.trimIndent()
        doTest(given, expected)
    }

    // Bad characters

    fun testBadCharacter() {
        val given = "LDA @"
        val expected = """
            OPCODE_TOKEN ('LDA')
            WHITE_SPACE (' ')
            BAD_CHARACTER ('@')
            """.trimIndent()
        doTest(given, expected)
    }
}