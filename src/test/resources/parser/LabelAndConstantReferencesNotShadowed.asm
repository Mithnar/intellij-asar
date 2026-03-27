org $008000

MyLabel:
NOP

!MyConst = $42
LDA MyLabel

LDA MyLabel+$10
LDA MyLabel*2

LDA !MyConst
LDA !MyConst+$08

dw MyLabel
dw MyLabel+$1000
dl !MyConst

if MyLabel > $8000
    NOP
endif

OtherLabel:
    LDA MyLabel+OtherLabel