org $008000

base $7E0000
base off

skip 5
skip -1
skip align 16
skip align 16 offset 5
skip align $20 offset $17

bank $FF
bank noassume
bank auto

dpbase $0200
optimize dp none
optimize dp ram
optimize dp always
optimize address default
optimize address ram
optimize address mirrors

pushpc
pullpc
pushbase
pullbase