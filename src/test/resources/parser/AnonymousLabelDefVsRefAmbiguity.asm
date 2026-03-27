org $008000
+
    LDA #$01
    BNE ++
    NOP
++
    LDA #$02
    BRA +
-
    LDA #$03
    DEX
    BNE -
    NOP
--
    LDA #$04
    BEQ --