; Verifies that the lexer captures multi-depth anonymous macro labels
; (?++, ?---, etc.) as a SINGLE token, with the depth encoded in the
; token text.
;
; Compare with the non-macro anonymous labels (`++`, `---`) which are
; assembled at the parser level from multiple PLUS_TOKEN / MINUS_TOKEN.
; Macro variants must NOT be split that way — they are atomic tokens.

macro nested_search_loop()
    ldx #$00
    ldy #$00

    ?-                  ; depth-1 backward anchor
        cpx #$10
        beq ?++         ; jumps PAST the depth-1 forward (to depth-2)

        ?--             ; depth-2 backward anchor (nested)
            cpy #$08
            beq ?+      ; exits the inner loop only
            iny
            bra ?--     ; loop inner
        ?+              ; close inner

        inx
        bra ?-          ; loop outer
    ?++                 ; depth-2 forward anchor (skipped to from above)

    rts
endmacro