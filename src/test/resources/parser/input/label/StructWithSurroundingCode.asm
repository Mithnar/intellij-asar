; Code surrounding struct definitions.

lorom
org $008000

struct ObjectEntry $7E1000
.State: skip 1
.Timer: skip 1
.PosX: skip 2
.PosY: skip 2
.Speed: skip 1
endstruct

!MAX_OBJECTS = 12

Main:
    jsr InitObjects
    jsr UpdateObjects
    rts

InitObjects:
    ldx #!MAX_OBJECTS - 1
-
    stz ObjectEntry[0].State,x
    dex
    bpl -
    rts

UpdateObjects:
.Loop:
    lda ObjectEntry.State
    beq .Skip
    jsr ProcessObject
.Skip:
    dex
    bpl .Loop
    rts

ProcessObject:
    lda ObjectEntry.PosX
    clc
    adc ObjectEntry.Speed
    sta ObjectEntry.PosX
    rts

struct TimerBlock $7E2000
.Active: skip 1
.Value: skip 2
endstruct

CheckTimer:
    lda TimerBlock.Active
    beq +
    dec TimerBlock.Value
+
    rts