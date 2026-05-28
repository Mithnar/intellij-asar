; Verifies that macro label references compose correctly with the full
; expression precedence chain — they must work as primaryExpression
; alongside numbers, constants, function calls, and parenthesized exprs.

macro layout_buffers()
    ?BufferBase = $7E0000
    ?BufferSize = $0100

    ?Slot0:
    ?.Header
    ?.Payload

    ?Slot1:
    ?.Header
    ?.Payload

    ; Simple addition with a macro label reference
    dl ?Slot0 + 1

    ; Macro label reference combined with constant reference
    !Stride = 4
    dl ?BufferBase + !Stride * 2

    ; Two macro label references in one expression
    dl ?Slot1 - ?Slot0

    ; Macro sub-label combined with macro main label arithmetic
    dl ?.Header + (?Slot1 - ?Slot0)

    ; Anonymous macro labels in arithmetic — references, not definitions,
    ; because they appear inside an expression context.
    ?-
        nop
        dl ?+ - ?-          ; size of region between the two anonymous labels
        nop
    ?+

    ; Mixed with prefix operators and shifts
    dl <?Slot0 >> 8
    dl -?BufferSize & $FF

    ; Macro label inside a function call argument
    dl bank_of(?Slot0) << 16

    ; Composite identifier reference participating in arithmetic
    dl ?Slot0_Header + ?BufferSize
endmacro

function bank_of(addr) = (addr >> 16) & $FF