package de.mithnar.plugin.asarasm.lexer

import com.intellij.lexer.Lexer
import com.intellij.psi.TokenType
import de.mithnar.plugin.asarasm.AsarLexerAdapter
import de.mithnar.plugin.asarasm.psi.AsarTypes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class AsarLexerTest {

    private fun createLexer(): Lexer = AsarLexerAdapter()

    private fun assertTokens(input: String, expected: String) {
        val lexer = createLexer()
        lexer.start(input)
        val actual = StringBuilder()
        while (lexer.tokenType != null) {
            val text = lexer.tokenText.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")
            actual.append("${lexer.tokenType} ('$text')\n")
            lexer.advance()
        }
        val actualStr = actual.toString().trim()
        val expectedStr = expected.trim()
        if (actualStr != expectedStr) {
            println("[DEBUG_LOG] Input: $input")
            println("[DEBUG_LOG] Expected:\n$expectedStr")
            println("[DEBUG_LOG] Actual:\n$actualStr")
        }
        assertEquals(expectedStr, actualStr)
    }

    @Test
    fun testSimpleOpcode() {
        assertTokens("LDA $00", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$00')")
    }

    @ParameterizedTest(name = "Opcode Smoke Test: {0}")
    @MethodSource("allOpcodes")
    fun testNoGaps(input: String) {
        val lexer = createLexer()
        lexer.start(input)
        var pos = 0
        while (lexer.tokenType != null) {
            assertEquals(pos, lexer.tokenStart, "Gap before token '${lexer.tokenText}' at position $pos in: $input")
            assertNotEquals(TokenType.BAD_CHARACTER, lexer.tokenType, "BAD_CHARACTER at position $pos: '${lexer.tokenText}' in: $input")
            pos = lexer.tokenEnd
            lexer.advance()
        }
        assertEquals(input.length, pos, "Lexer didn't consume entire input: $input")
    }

    @Test
    fun testRoundtrip() {
        val inputs = listOf(
            "LDA #$01",
            "Main:\n  LDA $00,x\n  RTS",
            "if !flag\n  db $00\nendif",
            "; comment\nLABEL: BRA .local",
            "pushpc : pullpc",
            "LDA.b $00",
            "JSL [!constant]"
        )
        for (input in inputs) {
            val lexer = createLexer()
            lexer.start(input)
            val reconstructed = StringBuilder()
            while (lexer.tokenType != null) {
                reconstructed.append(lexer.tokenText)
                lexer.advance()
            }
            assertEquals(input, reconstructed.toString(), "Roundtrip failed for: $input")
        }
    }

    @Test
    fun testIncrementalLexing() {
        val input = "LDA #$01 : RTS"
        val lexer = createLexer()
        
        // Lex normally
        lexer.start(input)
        while (lexer.tokenType != null && lexer.tokenText != ":") {
            lexer.advance()
        }
        
        val colonPos = lexer.tokenStart
        val state = lexer.state
        
        // Start from colon
        val incrementalLexer = createLexer()
        incrementalLexer.start(input, colonPos, input.length, state)
        
        assertEquals(AsarTypes.COLON_TOKEN, incrementalLexer.tokenType)
        assertEquals(":", incrementalLexer.tokenText)
        
        incrementalLexer.advance()
        assertEquals(TokenType.WHITE_SPACE, incrementalLexer.tokenType)
        
        incrementalLexer.advance()
        assertEquals(AsarTypes.IMPLIED_OPCODE_TOKEN, incrementalLexer.tokenType)
        assertEquals("RTS", incrementalLexer.tokenText)
    }

    @Test
    fun testAmbiguity() {
        // Opcode vs Identifier prefix
        assertTokens("LDA", "OPCODE_TOKEN ('LDA')")
        assertTokens("LDA_LABEL", "IDENTIFIER_TOKEN ('LDA_LABEL')")

        // PC keyword vs identifier
        assertTokens("PC_LABEL", "IDENTIFIER_TOKEN ('PC_LABEL')")
        assertTokens("PC", "IDENTIFIER_TOKEN ('PC')")
        
        // Width suffix vs identifier
        //TODO: assertTokens("LDA.b", "OPCODE_TOKEN ('LDA.b')")
        assertTokens("IDENT.b", "IDENTIFIER_TOKEN ('IDENT.b')")
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tokenSequenceData")
    fun testTokenSequences(name: String, input: String, expected: String) {
        assertTokens(input, expected)
    }

    companion object {
        @JvmStatic
        fun tokenSequenceData(): List<Array<Any>> = listOf(
            // Basic
            arrayOf("Simple Opcode", "LDA $00", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$00')"),
            arrayOf("Branch with Label", "BNE MyLabel", "BRANCH_OPCODE_TOKEN ('BNE')\nWHITE_SPACE (' ')\nIDENTIFIER_TOKEN ('MyLabel')"),
            arrayOf("Label Definition", "Main:", "LABEL_TOKEN ('Main:')"),
            arrayOf("Immediate Addressing", "LDA #$0F", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nHASH_TOKEN ('#')\nNUMBER_TOKEN ('$0F')"),
            arrayOf("Constant", "LDA !START_POS", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nCONSTANT_TOKEN ('!START_POS')"),
            arrayOf("Comment", "; hello comment", "COMMENT_TOKEN ('; hello comment')"),

            // Directives
            arrayOf("PushPullPc", "pushpc\npullpc", "IDENTIFIER_TOKEN ('pushpc')\nNEWLINE_TOKEN ('\\n')\nIDENTIFIER_TOKEN ('pullpc')"),
            arrayOf("Base Directive", "base \$008000", "IDENTIFIER_TOKEN ('base')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('\$008000')"),
            arrayOf("Base Off", "base off", "IDENTIFIER_TOKEN ('base')\nWHITE_SPACE (' ')\nIDENTIFIER_TOKEN ('off')"),
            arrayOf("IfElseEndif", "if !flag\nelse\nendif", "IDENTIFIER_TOKEN ('if')\nWHITE_SPACE (' ')\nCONSTANT_TOKEN ('!flag')\nNEWLINE_TOKEN ('\\n')\nIDENTIFIER_TOKEN ('else')\nNEWLINE_TOKEN ('\\n')\nIDENTIFIER_TOKEN ('endif')"),
            arrayOf("FillVsFillbyte", "fillbyte $00", "IDENTIFIER_TOKEN ('fillbyte')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$00')"),
            arrayOf("PadVsPadbyte", "padbyte \$FF", "IDENTIFIER_TOKEN ('padbyte')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('\$FF')"),
            arrayOf("WhileLoop", "while !counter > 0\nendwhile", "IDENTIFIER_TOKEN ('while')\nWHITE_SPACE (' ')\nCONSTANT_TOKEN ('!counter')\nWHITE_SPACE (' ')\nGT_TOKEN ('>')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('0')\nNEWLINE_TOKEN ('\\n')\nIDENTIFIER_TOKEN ('endwhile')"),
            arrayOf("Table as Identifier", "STA table,Y", "OPCODE_TOKEN ('STA')\nWHITE_SPACE (' ')\nIDENTIFIER_TOKEN ('table')\nCOMMA_TOKEN (',')\nIDENTIFIER_TOKEN ('Y')"),
            arrayOf("Data directive with data", "db \$FF,$00", "IDENTIFIER_TOKEN ('db')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('\$FF')\nCOMMA_TOKEN (',')\nNUMBER_TOKEN ('$00')"),

            // Keyword boundary / EOF fallback
            arrayOf("Opcode at EOF", "NOP", "IMPLIED_OPCODE_TOKEN ('NOP')"),
            arrayOf("Branch Opcode at EOF", "BRA", "BRANCH_OPCODE_TOKEN ('BRA')"),
            arrayOf("Jump Opcode at EOF", "JMP", "JUMP_OPCODE_TOKEN ('JMP')"),
            arrayOf("Directive at EOF", "org", "IDENTIFIER_TOKEN ('org')"),
            arrayOf("Opcode as Identifier Prefix", "LDA_TABLE", "IDENTIFIER_TOKEN ('LDA_TABLE')"),
            arrayOf("Branch Opcode as Identifier Prefix", "BNEflag", "IDENTIFIER_TOKEN ('BNEflag')"),
            arrayOf("Directive as Identifier Prefix", "organic", "IDENTIFIER_TOKEN ('organic')"),

            // Case insensitivity
            arrayOf("Case Insensitive Opcode", "lda $00", "OPCODE_TOKEN ('lda')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$00')"),
            arrayOf("Mixed Case Opcode", "Lda $00", "OPCODE_TOKEN ('Lda')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$00')"),
            arrayOf("Case Insensitive Registers", "X Y A", "IDENTIFIER_TOKEN ('X')\nWHITE_SPACE (' ')\nIDENTIFIER_TOKEN ('Y')\nWHITE_SPACE (' ')\nIDENTIFIER_TOKEN ('A')"),

            // Registers vs Identifiers
            arrayOf("PC keyword", "LDA pc", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nIDENTIFIER_TOKEN ('pc')"),
            arrayOf("Stack Register", "s", "IDENTIFIER_TOKEN ('s')"),
            arrayOf("Identifier starting with s", "sprite_table", "IDENTIFIER_TOKEN ('sprite_table')"),

            // Anonymous labels
            arrayOf("Anonymous Forward Label", "BRA ++", "BRANCH_OPCODE_TOKEN ('BRA')\nWHITE_SPACE (' ')\nPLUS_TOKEN ('+')\nPLUS_TOKEN ('+')"),
            arrayOf("Anonymous Backward Label", "BRA --", "BRANCH_OPCODE_TOKEN ('BRA')\nWHITE_SPACE (' ')\nMINUS_TOKEN ('-')\nMINUS_TOKEN ('-')"),

            // Addressing Mode Punctuation
            arrayOf("Indirect Indexed Tokens", "LDA ($10),Y", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nLPAREN_TOKEN ('(')\nNUMBER_TOKEN ('$10')\nRPAREN_TOKEN (')')\nCOMMA_TOKEN (',')\nIDENTIFIER_TOKEN ('Y')"),
            arrayOf("Long Indirect Indexed Tokens", "LDA [$10],Y", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nLBRACKET_TOKEN ('[')\nNUMBER_TOKEN ('$10')\nRBRACKET_TOKEN (']')\nCOMMA_TOKEN (',')\nIDENTIFIER_TOKEN ('Y')"),

            // Expressions with operators
            arrayOf("Prefix Operators", "LDA #<~\$FF", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nHASH_TOKEN ('#')\nLT_TOKEN ('<')\nTILDE_TOKEN ('~')\nNUMBER_TOKEN ('\$FF')"),
            arrayOf("Complex Expression", "LDA #$7E0000+!offset*2", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nHASH_TOKEN ('#')\nNUMBER_TOKEN ('$7E0000')\nPLUS_TOKEN ('+')\nCONSTANT_TOKEN ('!offset')\nSTAR_TOKEN ('*')\nNUMBER_TOKEN ('2')"),
            arrayOf("Bitwise Operators", "LDA #\$FF&$0F|$80^$01", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nHASH_TOKEN ('#')\nNUMBER_TOKEN ('\$FF')\nAMPERSAND_TOKEN ('&')\nNUMBER_TOKEN ('$0F')\nPIPE_TOKEN ('|')\nNUMBER_TOKEN ('$80')\nCARET_TOKEN ('^')\nNUMBER_TOKEN ('$01')"),

            // Strings
            arrayOf("String with escapes", "\"hello \\\"world\\\"\"", "STRING_TOKEN ('\"hello \\\"world\\\"\"')"),

            // Macro call
            arrayOf("Macro call tokens", "%MyMacro()", "PERCENT_TOKEN ('%')\nIDENTIFIER_TOKEN ('MyMacro')\nLPAREN_TOKEN ('(')\nRPAREN_TOKEN (')')"),

            // Constant definition
            arrayOf("Constant definition", "!myConst = $0F", "CONSTANT_TOKEN ('!myConst')\nWHITE_SPACE (' ')\nASSIGN_TOKEN ('=')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$0F')"),

            // Multiline / colon separator
            arrayOf("Colon separated instructions", "LDA #$00 : STA $10", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nHASH_TOKEN ('#')\nNUMBER_TOKEN ('$00')\nWHITE_SPACE (' ')\nCOLON_TOKEN (':')\nWHITE_SPACE (' ')\nOPCODE_TOKEN ('STA')\nWHITE_SPACE (' ')\nNUMBER_TOKEN ('$10')"),
            arrayOf("Newline is newline token", "NOP\nNOP", "IMPLIED_OPCODE_TOKEN ('NOP')\nNEWLINE_TOKEN ('\\n')\nIMPLIED_OPCODE_TOKEN ('NOP')"),

            // Bad characters
            arrayOf("Bad character", "LDA @", "OPCODE_TOKEN ('LDA')\nWHITE_SPACE (' ')\nBAD_CHARACTER ('@')")
        )

        @JvmStatic
        fun allOpcodes(): List<String> = listOf(
            "BRK", "PHP", "ASL A", "PHD", "CLC", "INC A", "TCS", "PLP", "ROL A", "PLD", "SEC", "DEC A", "TSC", "RTI", "PHA", "LSR A", "PHK", "CLI", "PHY", "TCD", "RTS", "PLA", "ROR A", "RTL", "SEI", "PLY", "TDC", "DEY", "TXA", "PHB", "TYA", "TXS", "TXY", "TAY", "TAX", "PLB", "CLV", "TSX", "TYX", "INY", "DEX", "WAI", "CLD", "PHX", "STP", "INX", "NOP", "XBA", "SED", "PLX", "XCE",
            "BRK #$12", "COP #$12", "ORA #$12", "ORA #$1234", "AND #$12", "AND #$1234", "EOR #$12", "EOR #$1234", "ADC #$12", "ADC #$1234", "BIT #$12", "BIT #$1234", "LDY #$12", "LDY #$1234", "LDX #$12", "LDX #$1234", "LDA #$12", "LDA #$1234", "CPY #$12", "CPY #$1234", "REP #$12", "CMP #$12", "CMP #$1234", "CPX #$12", "CPX #$1234", "SEP #$12", "SBC #$12", "SBC #$1234",
            "TSB $12", "ORA $12", "ASL $12", "BIT $12", "AND $12", "ROL $12", "EOR $12", "LSR $12", "STZ $12", "ADC $12", "ROR $12", "STY $12", "STA $12", "STX $12", "LDY $12", "LDA $12", "LDX $12", "CPY $12", "CMP $12", "DEC $12", "CPX $12", "SBC $12", "INC $12",
            "ORA $12,x", "ASL $12,x", "BIT $12,x", "AND $12,x", "ROL $12,x", "EOR $12,x", "LSR $12,x", "STZ $12,x", "ADC $12,x", "ROR $12,x", "STY $12,x", "STA $12,x", "LDY $12,x", "LDA $12,x", "CMP $12,x", "DEC $12,x", "SBC $12,x", "INC $12,x",
            "STX $12,y", "LDX $12,y",
            "TSB $1234", "ORA $1234", "ASL $1234", "BIT $1234", "AND $1234", "ROL $1234", "EOR $1234", "LSR $1234", "ADC $1234", "ROR $1234", "STY $1234", "STA $1234", "STX $1234", "STZ $1234", "LDY $1234", "LDA $1234", "LDX $1234", "CPY $1234", "CMP $1234", "DEC $1234", "CPX $1234", "SBC $1234", "INC $1234",
            "ORA $1234,x", "ASL $1234,x", "BIT $1234,x", "AND $1234,x", "ROL $1234,x", "EOR $1234,x", "LSR $1234,x", "ADC $1234,x", "ROR $1234,x", "STA $1234,x", "STZ $1234,x", "LDY $1234,x", "LDA $1234,x", "CMP $1234,x", "DEC $1234,x", "SBC $1234,x", "INC $1234,x",
            "ORA $1234,y", "AND $1234,y", "EOR $1234,y", "ADC $1234,y", "STA $1234,y", "LDA $1234,y", "LDX $1234,y", "CMP $1234,y", "SBC $1234,y",
            "ORA $123456", "AND $123456", "EOR $123456", "ADC $123456", "STA $123456", "LDA $123456", "CMP $123456", "SBC $123456",
            "ORA $123456,x", "AND $123456,x", "EOR $123456,x", "ADC $123456,x", "STA $123456,x", "LDA $123456,x", "CMP $123456,x", "SBC $123456,x",
            "ORA ($12)", "AND (\$12)", "EOR (\$12)", "ADC (\$12)", "STA (\$12)", "LDA (\$12)", "CMP (\$12)", "SBC (\$12)",
            "ORA (\$12),y", "AND (\$12),y", "EOR (\$12),y", "ADC (\$12),y", "STA (\$12),y", "LDA (\$12),y", "CMP (\$12),y", "SBC (\$12),y",
            "ORA (\$12,x)", "AND (\$12,x)", "EOR (\$12,x)", "ADC (\$12,x)", "STA (\$12,x)", "LDA (\$12,x)", "CMP (\$12,x)", "SBC (\$12,x)",
            "ORA [\$12]", "AND [\$12]", "EOR [\$12]", "ADC [\$12]", "STA [\$12]", "LDA [\$12]", "CMP [\$12]", "SBC [\$12]",
            "ORA [\$12],y", "AND [\$12],y", "EOR [\$12],y", "ADC [\$12],y", "STA [\$12],y", "LDA [\$12],y", "CMP [\$12],y", "SBC [\$12],y",
            "ORA \$12,s", "AND \$12,s", "EOR \$12,s", "ADC \$12,s", "STA \$12,s", "LDA \$12,s", "CMP \$12,s", "SBC \$12,s",
            "ORA (\$12,s),y", "AND (\$12,s),y", "EOR (\$12,s),y", "ADC (\$12,s),y", "STA (\$12,s),y", "LDA (\$12,s),y", "CMP (\$12,s),y", "SBC (\$12,s),y",
            "BPL $12", "BMI $12", "BVC $12", "BVS $12", "BCC $12", "BCS $12", "BNE $12", "BEQ $12", "BRA $12", "BRL $1234",
            "JMP $1234", "JMP ($1234)", "JMP ($1234,x)", "JML $123456", "JML [$1234]", "JSR $1234", "JSR ($1234,x)", "JSL $123456",
            "MVP $12,$34", "MVN $12,$34", "PEA $1234", "PEI ($12)", "PER $1234", "WDM", "WDM #$12", "COP"
        )
    }
}
