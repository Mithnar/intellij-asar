package de.mithnar.plugin.asarasm.annotator

import de.mithnar.plugin.asarasm.annotator.AddressingMode.*

object OpcodeAddressingModes {
    val REPEAT_OPCODES = setOf("asl", "lsr", "rol", "ror", "inc", "dec")

    val ALLOWED_MODES: Map<String, Set<AddressingMode>> = mapOf(

        // Implied (no operand)
        // PHP, PLP, PHA, PHX, PHY, PHB, PHD, PHK
        // PLA, PLX, PLY, PLB, PLD, PLP
        // TAX, TAY, TSX, TXA, TXS, TYA, TXY, TYX, TCD, TDC, TCS, TSC, XCE, XBA
        // CLC, SEC, CLI, SEI, CLV, CLD, SED
        // DEX, DEY, INX, INY
        // RTI, RTS, RTL
        // NOP, STP, WAI
        // !!! handled in grammar by impliedInstruction, no operand validation needed

        // Optional immediate
        "brk" to setOf(IMMEDIATE),
        "cop" to setOf(IMMEDIATE),
        "wdm" to setOf(IMMEDIATE),

        // ALU
        "ora" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "and" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "eor" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "adc" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "sbc" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "cmp" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "lda" to setOf(
            IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),
        "sta" to setOf(
            ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y,
            INDEXED_INDIRECT, INDIRECT, INDIRECT_INDEXED,
            LONG_INDIRECT, LONG_INDIRECT_INDEXED,
            STACK_RELATIVE, STACK_RELATIVE_INDIRECT_INDEXED
        ),

        // Shift / Rotate / Inc / Dec
        "asl" to setOf(ACCUMULATOR, ABSOLUTE, ABSOLUTE_X, REPEAT_COUNT),
        "lsr" to setOf(ACCUMULATOR, ABSOLUTE, ABSOLUTE_X, REPEAT_COUNT),
        "rol" to setOf(ACCUMULATOR, ABSOLUTE, ABSOLUTE_X, REPEAT_COUNT),
        "ror" to setOf(ACCUMULATOR, ABSOLUTE, ABSOLUTE_X, REPEAT_COUNT),
        "inc" to setOf(ACCUMULATOR, ABSOLUTE, ABSOLUTE_X, REPEAT_COUNT),
        "dec" to setOf(ACCUMULATOR, ABSOLUTE, ABSOLUTE_X, REPEAT_COUNT),

        // Bit test / set / reset
        "bit" to setOf(IMMEDIATE, ABSOLUTE, ABSOLUTE_X),             // ← IMMEDIATE added
        "tsb" to setOf(ABSOLUTE),                                    // dp or absolute only
        "trb" to setOf(ABSOLUTE),

        // LDX / STX
        "ldx" to setOf(IMMEDIATE, ABSOLUTE, ABSOLUTE_Y),
        "stx" to setOf(ABSOLUTE, ABSOLUTE_Y),
        "ldy" to setOf(IMMEDIATE, ABSOLUTE, ABSOLUTE_X),
        "sty" to setOf(ABSOLUTE, ABSOLUTE_X),

        // ── STZ ─────────────────────────────────────────────────────────────────
        "stz" to setOf(ABSOLUTE, ABSOLUTE_X),

        // ── Compare ─────────────────────────────────────────────────────────────
        "cpx" to setOf(IMMEDIATE, ABSOLUTE),                         // no indexing
        "cpy" to setOf(IMMEDIATE, ABSOLUTE),

        // ── Immediate-only ───────────────────────────────────────────────────────
        "rep" to setOf(IMMEDIATE),
        "sep" to setOf(IMMEDIATE),

        // ── Stack push/pull specials ─────────────────────────────────────────────
        "pea" to setOf(ABSOLUTE),                                    // bare $1234 (no #)
        "pei" to setOf(INDIRECT),                                    // ($12) only
        "per" to setOf(ABSOLUTE),                                    // relative label/expr (bare)

        // ── Jump / call ──────────────────────────────────────────────────────────
        // handled by jumpInstruction grammar rule; annotator can refine if needed
        // "jmp" → ABSOLUTE, INDIRECT, INDEXED_INDIRECT
        // "jml" → ABSOLUTE, LONG_INDIRECT
        // "jsr" → ABSOLUTE, INDEXED_INDIRECT
        // "jsl" → ABSOLUTE only

        // ── Block move ───────────────────────────────────────────────────────────
        // handled structurally by blockMoveInstruction
    )
}