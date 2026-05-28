; Tests sub-labels at arbitrary depths.
; Syntax: .name, ..name, ...name, ....name, etc.
; Colon is optional on sub-label definitions.

org $008000

StateMachine:
    jsr StateMachine_Idle
    jsr StateMachine_Idle_Enter
    jmp StateMachine_Run_Loop_Body_Tick

.Idle                       ; no colon
    lda #$00
..Enter:                    ; with colon
    sta $10
..Exit
    rts

.Run:
    lda #$01
..Loop
...Body:
....Tick:
    inc $10
    bne ....Tick            ; deepest
    bra ...Body
.....DeepEdge:              ; 5 dots — arbitrary depth edge case
    rts

; New main label resets sub group
Other:
    rts
.Idle:                      ; same sub name as above — different group
    rts

Table:
    dl StateMachine_Idle
    dl StateMachine_Idle_Enter
    dl StateMachine_Run_Loop_Body_Tick
    dl StateMachine_Run_Loop_Body_Tick_DeepEdge
    dl Other_Idle