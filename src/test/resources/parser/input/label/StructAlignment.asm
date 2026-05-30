; Struct with alignment — the endstruct align clause changes effective size.

struct Sprite $7E0200
.Id: skip 1
.X: skip 2
.Y: skip 2
.Flags: skip 1
endstruct align 8

; Plain field access
lda Sprite.Id
lda Sprite.X
lda Sprite.Flags

; Array-indexed access
lda Sprite[0].Id
lda Sprite[1].X
lda Sprite[3].Flags

; Multiple structs with different alignments
struct Header $7E0000
.Magic: skip 2
.Version: skip 1
endstruct align 4

struct Trailer $7E0010
.Checksum: skip 2
endstruct align 16

lda Header.Magic
lda Header.Version
lda Trailer.Checksum