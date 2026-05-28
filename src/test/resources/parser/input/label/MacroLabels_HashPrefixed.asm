; Verifies that #?... forms parse as macro labels, NOT as global labels.
; The `pin=2` change to globalLabelDefinition is what makes this work:
; after consuming HASH_TOKEN, the parser must be able to back out when
; the next token is MACRO_LABEL_TOKEN / MACRO_LOCAL_LABEL_TOKEN instead
; of LABEL_TOKEN / IDENTIFIER_TOKEN.

arch 65816
lorom

macro setup_player_state()
    #?PlayerInit:
        lda #$01
        sta $7E0010

    #?.LoadDefaults
        lda #$00
        sta $7E0011
        sta $7E0012

    ; Mix of hashed and non-hashed forms in the same macro
    ?RegularMainLabel:
        rts
endmacro

; Contrast: a real global label (no `?`), should still parse as before.
#GlobalRoutine:
    %setup_player_state()
    rtl