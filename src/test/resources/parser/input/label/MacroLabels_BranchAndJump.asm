; Verifies that macro label references flow correctly through:
;   - branchTarget        (e.g. `bra ?+`, `beq ?MainLabel`)
;   - jumpTarget          (e.g. `jmp ?MainLabel`)
;   - jumpTarget's indexed-indirect form `(expr,X)` — exercises wiring of
;     macroLabelReference / macroLocalLabelReference / macroAnonymousLabelReference
;     through symbolReferenceTarget and into the addressing-mode rules.

macro dispatch_handler()
    ?Entry:
        lda $00
        beq ?+              ; branch to macro anonymous forward
        cmp #$01
        beq ?HandleOne      ; branch to macro main label
        cmp #$02
        beq ?.HandleTwo     ; branch to macro sub-label
        bra ?-              ; branch to macro anonymous backward

    ?-                      ; the backward target
        rts

    ?HandleOne:
        jmp ?Entry          ; jump to macro main label
        jsr ?HandleOne_Continue ; jump to "composite" reference (single token)

    ?.HandleTwo
        ; Indexed-indirect with a macro label reference inside the parens
        jmp (?JumpTable,x)
        rts

    ?.HandleTwo_Continue
        rts

    ?HandleOne_Continue:
        rts

    ?+                      ; the forward target
        rts

    ?JumpTable:
        dl ?HandleOne
        dl ?.HandleTwo
        dl ?HandleOne_Continue
endmacro