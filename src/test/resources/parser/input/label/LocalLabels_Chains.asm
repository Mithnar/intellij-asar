; Tests +/- relative labels.
; Single and chained, with/without optional colon.

org $008000

    ldx.b #4

--                  ; :neg_2_1
    lda $10,x
    beq +           ; forward to next single +

    ldy.b #8

-                   ; :neg_1_1
    jsr DoSomething
    dey
    bne -           ; back to previous single -

+:                  ; :pos_1_0 — colon form
    dex
    bpl --          ; back to previous --

DoSomething:
    rts

; Chained +/- with various lengths, including stacked declarations
Mixer:
    bra ++
    bra +
    bra +++

---                 ; :neg_3_1
-                   ; :neg_1_2
    bra -
--                  ; :neg_2_2
-                   ; :neg_1_3
    bra ---
    bra --
    bra -

++                  ; :pos_2_0
+                   ; :pos_1_0
    bra ++
++:                 ; :pos_2_1 (colon ok)
+++                 ; :pos_3_0
    rts

; Edge: very long chains
++++++++:
    bra --------
--------
    rts