org $008000

SimpleLabel:
    NOP

namespace MyNS
MyNS.SubLabel:
    NOP
namespace off

OuterLabel:
.local:
    NOP
    BRA .local
    JSR MyNS.SubLabel

function MyNS.double(x) = x*2
    LDA #MyNS.double(3)

macro MyNS.init(val)
    LDA #<val>
endmacro
    %MyNS.init($01)

namespace MyNS
namespace nested on
namespace nested off
namespace off

namespace Patch
Patch.Routine:
    RTS

namespace off
autoclean JSL Patch.Routine

; --- width-suffixed opcodes must stay as opcodes ---
    LDA.b $00
    LDA.w $0000
    BRA.w .local

; --- namespace labels sharing opcode prefix ---
namespace LDA
LDA.bank:
    NOP
namespace off
    JML LDA.bank

; --- single-char non-suffix segment and register vs namespace ---
namespace a
a.b:
    NOP
a.c:
    NOP
namespace off
    LDA a.b
    LDA a.c
    ASL A
    LDA $10,X