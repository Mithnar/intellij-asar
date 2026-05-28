; Mixed label kinds and parser edge cases.
; - Labels immediately followed by code on the next line
; - Labels with/without trailing colons where allowed
; - Comments at end of label lines
; - Multiple labels stacked back-to-back
; - Sub label right after a +/- label (the +/- doesn't change the main group)
; - Label assignment interleaved with code

org $008000

; Stacked labels (no code between them)
EntryA:
EntryB:
EntryC:
    rts

; Label with trailing comment
RenderFrame:                ; called every NMI
    php
.Push                       ; sub, no colon
    rep #$30
.Body:                      ; sub, colon
-                           ; +/- inside a main group
    lda $2140
    bne -
.Pop:
    plp
    rtl

; Assignment in the middle
PPU_BRIGHTNESS = $2100
NMITIMEN       = $4200

; +/- label does NOT change the current main group "RenderFrame"
+
    nop
.StillRenderFrameSub:       ; still a sub of RenderFrame
    rts

; New main label starts a fresh sub group
Audio:
.Init:
    rts
.Tick:
    rts

; Edge: identifier that looks numeric-ish but starts with a letter
A0:
    rts
_:                          ; single underscore identifier
    rts

; Table mixing all kinds of references
Table:
    dl Main                 ; (undefined here — semantics not checked)
    dl RenderFrame
    dl RenderFrame_Body
    dl RenderFrame_StillRenderFrameSub
    dl Audio_Init
    dl Audio_Tick
    dl PPU_BRIGHTNESS
    dl A0