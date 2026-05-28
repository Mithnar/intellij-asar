; Negative test: whitespace between `?` and the suffix MUST NOT lex as a
; macro label.
;
; The lexer patterns for MACRO_LABEL / MACRO_IDENTIFIER / MACRO_LOCAL_LABEL /
; MACRO_PLUS_LABEL / MACRO_MINUS_LABEL all require `?` to be immediately
; adjacent to the following character (no whitespace allowed). When
; whitespace intervenes, the `?` has no matching rule on its own — it
; should be reported as BAD_CHARACTER (since `?` is not a standalone token
; in Asar; it only appears as part of `?=` or the macro-label tokens).
;
; This file is expected to produce multiple BAD_CHARACTER markers, NOT
; macro-label PSI elements. The parser tree should still recover and
; continue past each bad line.

macro broken_syntax()
    ; `? Foo:` — NOT a macro label (space between `?` and `Foo:`)
    ? Foo:

    ; `? .Sub` — NOT a macro sub-label (space between `?` and `.Sub`)
    ? .Sub

    ; `? +` and `? -` — NOT macro anonymous labels (space between `?` and `+`/`-`)
    ? +
    ? -

    ; A well-formed macro label after the broken lines, to confirm that
    ; the parser recovers and the rest of the macro still parses cleanly.
    ?GoodLabel:
        rts
endmacro