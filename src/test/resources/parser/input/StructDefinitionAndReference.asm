struct TestObjectOne $7E0100
    .Type: skip 1
    .PosX: skip 2
    .PosY: skip 2
    .SizeX: skip 1
    .SizeY: skip 1
endstruct

struct TestObjectTwo $7E0200
    .Type: skip 1
    .PosX: skip 2
    .PosY: skip 2
    .SizeX: skip 1
    .SizeY: skip 1
endstruct align 16

lda TestObjectOne.PosY
lda ObjectList[2].PosY