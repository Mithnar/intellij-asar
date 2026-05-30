struct ObjectList $7E0100
.Type: skip 1
.PosX: skip 2
.PosY: skip 2
.SizeX: skip 1
.SizeY: skip 1
endstruct

struct Properties extends ObjectList
.Palette: skip 1
.TileNumber: skip 2
.FlipX: skip 1
.FlipY: skip 1
endstruct

lda ObjectList.Properties.FlipX
lda ObjectList[2].Properties.FlipX
lda ObjectList.Properties[2].FlipX