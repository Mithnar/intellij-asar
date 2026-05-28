; Tests basic main label parsing.
; Main labels: global, identifier chars [a-zA-Z0-9_], colon required.

org $008000

Main:
    jsr Init
    jsr GameLoop
    jmp Main

Init:
    sei
    clc
    xce
    rep #$30
    rts

GameLoop:
    jsr UpdatePlayer
    jsr UpdateEnemies_07
    jsr _internalHelper
    rts

UpdatePlayer:
    rts

UpdateEnemies_07:
    rts

_internalHelper:
    rts

; Edge: identifier with digits and underscores, but not starting with digit
Label_123_abc_XYZ:
    rts