namespace nested on
Main:                           ; Main
Main2:                          ; Main2
namespace Deep
    Main:                       ; Deep_Main
    namespace Deeper
        Main:                   ; Deep_Deeper_Main
        dl Main                 ; Deep_Deeper_Main
        dl Main2                ; Main2
    namespace off
    dl Main                     ; Deep_Main
namespace off