    ldx.b #4
--
    lda $10,x
    beq +
    ldy.b #8

-
    %do_something()
    dey
    bne -
+:
    dex
    bpl --