; Tests the `#` prefix on label definitions.
; `#Label:` defines a main label without modifying the current sub group.
; `#.Sub:` is allowed by syntax but discouraged.
; Note: label assignments cannot be prefixed with `#`.

org $008000

macro install_routine()
    jsl MyNewRoutine

    !routine_defined ?= 0
    if !routine_defined == 0
        pushpc
        freecode cleaned

        #MyNewRoutine:                  ; global, doesn't disturb caller's group
            incsrc "routines/new.asm"
            rts

        #HelperA:
            nop
            rts

        pullpc
        !routine_defined = 1
    endif
endmacro

Main:
    %install_routine()
.Sub:                                   ; still belongs to Main, not MyNewRoutine
    nop

    ; Both should resolve
    dl MyNewRoutine
    dl Main_Sub

; Edge: hash-prefixed sub label (legal syntax, unusual)
Outer:
    nop
#.WeirdSub:
    rts