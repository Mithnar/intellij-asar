; Tests main labels defined via assignment (label = address).
; Assignment form does NOT start a new sub-label group.

org $008000

Main:
    jsl SomewhereInRom
    jsl Main_Sub           ; valid: Main has a sub group
    rts

.Sub:
    rts

; Label assignments — various RHS forms
SomewhereInRom    = $04CA40
VRAM_BG1          = $2107
OAM_BASE          = $2101
ComputedAddr      = $008000+$200
MaskedAddr        = ($7E0000|$1F00)&$FFFFFF
ShiftedAddr       = $7E<<16|$0100

; Assignment must NOT start a new sub group — .Sub below still belongs to Main2
Main2:
    rts
AnotherAssigned = $00FFEE
.Sub:
    rts

Table:
    dl Main_Sub             ; ok
    dl Main2_Sub            ; ok — assignment didn't break the group
    dl SomewhereInRom_Sub   ; intentionally "wrong" — semantics not checked