macro my_new_routine()
    jsl MyNewRoutine
    !macro_routine_defined ?= 0
    if !macro_routine_defined == 0
        pushpc
        freecode cleaned
        #MyNewRoutine:
            incsrc routines/mynewroutine.asm
        pullpc
        !macro_routine_defined = 1
    endif
endmacro

Main:
    %my_new_routine()
.Sub
    dl MyNewRoutine
    dl Main_Sub