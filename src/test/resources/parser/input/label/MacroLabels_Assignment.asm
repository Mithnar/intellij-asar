; Verifies macro label assignment: ?Name = <expression>
;
; Key parser behavior under test:
;   - `pin=2` on macroLabelAssignment commits only after MACRO_IDENTIFIER_TOKEN
;     followed by ASSIGN_TOKEN. A bare `?Name` at top level (not followed by
;     `=`) must NOT be mis-committed as a stalled assignment.
;   - The right-hand side is a full expression, so arithmetic and references
;     must compose normally.

macro define_slots()
    ; Simple address assignment
    ?PlayerSlot = $7E0000

    ; RHS is an arithmetic expression
    ?EnemySlot  = ?PlayerSlot + $0100

    ; RHS pulls in a constant reference and a function call
    !BaseSlot = $7E2000
    ?ItemSlot = !BaseSlot + sizeof_item() * 4

    ; A definition (with colon) on the very next line — must not be confused
    ; with the assignment form on the line above.
    ?MainEntry:
        lda.w #?PlayerSlot
        sta $00

    ; A bare reference on its own line is illegal at top level, but a
    ; reference inside an expression is fine — verify the parser doesn't
    ; accidentally treat `?PlayerSlot` here as a stalled assignment LHS.
    dl ?PlayerSlot
endmacro

function sizeof_item() = 8