Proc1:
    nop
.Sub
    bra .Sub
Proc2:
    nop
.Sub:
    bra .Sub
..Deeper:
    nop
...TheEnd:
    nop
Table:
    dl Proc2_Sub_Deeper_TheEnd
