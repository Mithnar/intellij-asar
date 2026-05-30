; Struct field references used in various expression contexts.

struct OAM $0200
.TileIndex: skip 1
.Attr: skip 1
.X: skip 2
.Y: skip 2
endstruct

; In data directives
dl OAM.TileIndex
dw OAM.X
db OAM.Attr

; In arithmetic expressions
dl OAM.X + 1
dl OAM.Y - OAM.X
dl OAM.TileIndex * 2

; In constant definition
!OAM_Y_OFFSET = OAM.Y

; In assert
assert OAM.Y == 4, "OAM.Y offset must be 4"

; Indexed array access in expressions
dl OAM[0].X
dl OAM[2].Y + $10

; Struct field used as jump target address value
org $008000
Main:
    lda OAM.TileIndex
    jmp OAM.X

; Struct field inside conditional
if OAM.X == 4
    lda #$01
endif