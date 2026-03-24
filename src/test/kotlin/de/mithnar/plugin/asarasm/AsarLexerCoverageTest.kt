package de.mithnar.plugin.asarasm

import com.intellij.psi.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class AsarLexerCoverageTest {

    private fun assertNoGaps(input: String) {
        val lexer = AsarLexerAdapter()
        lexer.start(input)
        var pos = 0
        while (lexer.tokenType != null) {
            assertEquals(
                pos, lexer.tokenStart,
                "Gap before token '${lexer.tokenText}' at position $pos in: $input"
            )
            assertNotEquals(
                TokenType.BAD_CHARACTER, lexer.tokenType,
                "BAD_CHARACTER at position $pos: '${lexer.tokenText}' in: $input"
            )
            pos = lexer.tokenEnd
            lexer.advance()
        }
        assertEquals(
            input.length, pos,
            "Lexer didn't consume entire input (stopped at $pos of ${input.length}): $input"
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allInstructions")
    fun testNoCoverageGaps(input: String) {
        assertNoGaps(input)
    }

    companion object {
        @JvmStatic
        fun allInstructions(): List<String> = listOf(
            // === Implied / Accumulator ===
            "BRK",
            "PHP",
            "ASL A",
            "PHD",
            "CLC",
            "INC A",
            "TCS",
            "PLP",
            "ROL A",
            "PLD",
            "SEC",
            "DEC A",
            "TSC",
            "RTI",
            "PHA",
            "LSR A",
            "PHK",
            "CLI",
            "PHY",
            "TCD",
            "RTS",
            "PLA",
            "ROR A",
            "RTL",
            "SEI",
            "PLY",
            "TDC",
            "DEY",
            "TXA",
            "PHB",
            "TYA",
            "TXS",
            "TXY",
            "TAY",
            "TAX",
            "PLB",
            "CLV",
            "TSX",
            "TYX",
            "INY",
            "DEX",
            "WAI",
            "CLD",
            "PHX",
            "STP",
            "INX",
            "NOP",
            "XBA",
            "SED",
            "PLX",
            "XCE",

            // === Immediate ===
            "BRK #$12",
            "COP #$12",
            "ORA #$12",
            "ORA #$1234",
            "AND #$12",
            "AND #$1234",
            "EOR #$12",
            "EOR #$1234",
            "ADC #$12",
            "ADC #$1234",
            "BIT #$12",
            "BIT #$1234",
            "LDY #$12",
            "LDY #$1234",
            "LDX #$12",
            "LDX #$1234",
            "LDA #$12",
            "LDA #$1234",
            "CPY #$12",
            "CPY #$1234",
            "REP #$12",
            "CMP #$12",
            "CMP #$1234",
            "CPX #$12",
            "CPX #$1234",
            "SEP #$12",
            "SBC #$12",
            "SBC #$1234",

            // === Direct Page ===
            "TSB $12",
            "ORA $12",
            "ASL $12",
            "BIT $12",
            "AND $12",
            "ROL $12",
            "EOR $12",
            "LSR $12",
            "STZ $12",
            "ADC $12",
            "ROR $12",
            "STY $12",
            "STA $12",
            "STX $12",
            "LDY $12",
            "LDA $12",
            "LDX $12",
            "CPY $12",
            "CMP $12",
            "DEC $12",
            "CPX $12",
            "SBC $12",
            "INC $12",

            // === Direct Page, X-indexed ===
            "ORA $12,x",
            "ASL $12,x",
            "BIT $12,x",
            "AND $12,x",
            "ROL $12,x",
            "EOR $12,x",
            "LSR $12,x",
            "STZ $12,x",
            "ADC $12,x",
            "ROR $12,x",
            "STY $12,x",
            "STA $12,x",
            "LDY $12,x",
            "LDA $12,x",
            "CMP $12,x",
            "DEC $12,x",
            "SBC $12,x",
            "INC $12,x",

            // === Direct Page, Y-indexed ===
            "STX $12,y",
            "LDX $12,y",

            // === Absolute ===
            "TSB $1234",
            "ORA $1234",
            "ASL $1234",
            "BIT $1234",
            "AND $1234",
            "ROL $1234",
            "EOR $1234",
            "LSR $1234",
            "ADC $1234",
            "ROR $1234",
            "STY $1234",
            "STA $1234",
            "STX $1234",
            "STZ $1234",
            "LDY $1234",
            "LDA $1234",
            "LDX $1234",
            "CPY $1234",
            "CMP $1234",
            "DEC $1234",
            "CPX $1234",
            "SBC $1234",
            "INC $1234",

            // === Absolute, X-indexed ===
            "ORA $1234,x",
            "ASL $1234,x",
            "BIT $1234,x",
            "AND $1234,x",
            "ROL $1234,x",
            "EOR $1234,x",
            "LSR $1234,x",
            "ADC $1234,x",
            "ROR $1234,x",
            "STA $1234,x",
            "STZ $1234,x",
            "LDY $1234,x",
            "LDA $1234,x",
            "CMP $1234,x",
            "DEC $1234,x",
            "SBC $1234,x",
            "INC $1234,x",

            // === Absolute, Y-indexed ===
            "ORA $1234,y",
            "AND $1234,y",
            "EOR $1234,y",
            "ADC $1234,y",
            "STA $1234,y",
            "LDA $1234,y",
            "LDX $1234,y",
            "CMP $1234,y",
            "SBC $1234,y",

            // === Long (24-bit) ===
            "ORA $123456",
            "AND $123456",
            "EOR $123456",
            "ADC $123456",
            "STA $123456",
            "LDA $123456",
            "CMP $123456",
            "SBC $123456",

            // === Long, X-indexed ===
            "ORA $123456,x",
            "AND $123456,x",
            "EOR $123456,x",
            "ADC $123456,x",
            "STA $123456,x",
            "LDA $123456,x",
            "CMP $123456,x",
            "SBC $123456,x",

            // === Direct Page Indirect (dp) ===
            "ORA ($12)",
            "AND ($12)",
            "EOR ($12)",
            "ADC ($12)",
            "STA ($12)",
            "LDA ($12)",
            "CMP ($12)",
            "SBC ($12)",

            // === DP Indirect Indexed, Y: (dp),y ===
            "ORA ($12),y",
            "AND ($12),y",
            "EOR ($12),y",
            "ADC ($12),y",
            "STA ($12),y",
            "LDA ($12),y",
            "CMP ($12),y",
            "SBC ($12),y",

            // === DP Indexed Indirect, X: (dp,x) ===
            "ORA ($12,x)",
            "AND ($12,x)",
            "EOR ($12,x)",
            "ADC ($12,x)",
            "STA ($12,x)",
            "LDA ($12,x)",
            "CMP ($12,x)",
            "SBC ($12,x)",

            // === DP Indirect Long: [dp] ===
            "ORA [$12]",
            "AND [$12]",
            "EOR [$12]",
            "ADC [$12]",
            "STA [$12]",
            "LDA [$12]",
            "CMP [$12]",
            "SBC [$12]",

            // === DP Indirect Long Indexed, Y: [dp],y ===
            "ORA [$12],y",
            "AND [$12],y",
            "EOR [$12],y",
            "ADC [$12],y",
            "STA [$12],y",
            "LDA [$12],y",
            "CMP [$12],y",
            "SBC [$12],y",

            // === Stack Relative: sr,s ===
            "ORA $12,s",
            "AND $12,s",
            "EOR $12,s",
            "ADC $12,s",
            "STA $12,s",
            "LDA $12,s",
            "CMP $12,s",
            "SBC $12,s",

            // === Stack Relative Indirect Indexed: (sr,s),y ===
            "ORA ($12,s),y",
            "AND ($12,s),y",
            "EOR ($12,s),y",
            "ADC ($12,s),y",
            "STA ($12,s),y",
            "LDA ($12,s),y",
            "CMP ($12,s),y",
            "SBC ($12,s),y",

            // === Branches ===
            "BPL $12",
            "BMI $12",
            "BVC $12",
            "BVS $12",
            "BCC $12",
            "BCS $12",
            "BNE $12",
            "BEQ $12",
            "BRA $12",
            "BRL $1234",

            // === Jumps ===
            "JMP $1234",
            "JMP ($1234)",
            "JMP ($1234,x)",
            "JML $123456",
            "JML [$1234]",
            "JSR $1234",
            "JSR ($1234,x)",
            "JSL $123456",

            // === Block Move ===
            "MVP $12,$34",
            "MVN $12,$34",

            // === Stack Push/Pull (with operand) ===
            "PEA $1234",
            "PEI ($12)",
            "PER $1234",

            // === WDM ===
            "WDM",
            "WDM #$12",
            "COP"
        )
    }
}