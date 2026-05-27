package de.mithnar.plugin.asarasm;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import de.mithnar.plugin.asarasm.psi.AsarTypes;

%%

%class AsarLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%caseless
%eof{ return;
%eof}

%{
  private boolean isAtEnd() {
    return zzCurrentPos >= zzEndRead;
  }
%}

%xstate CONTINUATION_BACKSLASH
%xstate CONTINUATION_COMMA
%xstate COMMA_PENDING_CONTINUATION

//Compatibility

ASAR_VERSION = [0-9]+\.[0-9]+

// Character classes

IDENTIFIER_START = [A-Za-z_]
IDENTIFIER_PART  = [A-Za-z0-9_]
NOT_IDENT_PART   = [^A-Za-z0-9_]
NOT_IDENT_OR_DOT_PART = [^A-Za-z0-9_.]

// Composite tokens

COMMENT                 = ";"[^\n]*
LABEL                   = {IDENTIFIER_START}{IDENTIFIER_PART}*":"
LOCAL_LABEL             = "."+{IDENTIFIER_PART}+":"?
CONSTANT                = "!"{IDENTIFIER_PART}+
IDENTIFIER              = {IDENTIFIER_START}{IDENTIFIER_PART}*
NAMESPACED_IDENTIFIER   = {IDENTIFIER}("."{IDENTIFIER_PART}+)+
NUMBER_HEX              = \$[0-9A-Fa-f]+
NUMBER_DEC              = [0-9]+
NUMBER_BIN              = %[01]+
MACRO_PARAM             = "<"{IDENTIFIER_PART}+">"
STRING                  = \"([^\"\\\n]|\\.)*\"

// Whitespace

WHITESPACE = [ \t]+
NEWLINE    = \r?\n

// Operators

ASSIGN          = "="
PLUS_ASSIGN     = "+="
COLON_ASSIGN    = ":="
HASH_ASSIGN     = "#="
QUESTION_ASSIGN = "?="

COLON       = ":"
COMMA       = ","
HASH        = "#"
LPAREN      = "("
RPAREN      = ")"
LBRACKET    = "["
RBRACKET    = "]"

PLUS        = "+"
MINUS       = "-"
STAR        = "*"
SLASH       = "/"
PERCENT     = "%"
SHIFT_LEFT  = "<<"
SHIFT_RIGHT = ">>"

EQ          = "=="
NEQ         = "!="
LT          = "<"
GT          = ">"
LTE         = "<="
GTE         = ">="

LOGICAL_AND = "&&"
LOGICAL_OR  = "||"
PIPE        = "|"
AMPERSAND   = "&"
CARET       = "^"
TILDE       = "~"

// Opcodes

WIDTH_SUFFIX = ".b" | ".w" | ".l"

OPCODE =
    // ALU / memory (may have operand)
   ("ora"|"and"|"eor"|"adc"|"sbc"|"cmp"|
    "asl"|"lsr"|"rol"|"ror"|"inc"|"dec"|
    "bit"|"trb"|"tsb"|
    "brk"|"cop"|"wdm"|
    "lda"|"ldx"|"ldy"|"sta"|"stx"|"sty"|"stz"|
    "cpx"|"cpy"|"rep"|"sep"|"pea"|"pei"|"per") {WIDTH_SUFFIX}?

IMPLIED_OPCODE =
    // Truly implied (no operand ever)
    ("nop"|"wai"|"stp"|
    "rts"|"rtl"|"rti"|
    "pha"|"phx"|"phy"|"phb"|"phd"|"phk"|
    "pla"|"plx"|"ply"|"plb"|"pld"|"plp"|"php"|
    "tax"|"tay"|"tsx"|"txa"|"txs"|"tya"|"txy"|"tyx"|"tcd"|"tdc"|"tcs"|"tsc"|"xce"|"xba"|
    "clc"|"sec"|"cli"|"sei"|"clv"|"cld"|"sed"|
    "dex"|"dey"|"inx"|"iny")

BLOCK_MOVE_OPCODE = "mvp"|"mvn"

BRANCH_OPCODE = ("bpl"|"bmi"|"bvc"|"bvs"|"bcc"|"bcs"|"bne"|"beq"|"bra"|"brl") {WIDTH_SUFFIX}?

JUMP_OPCODE = ("jsr"|"jsl"|"jml"|"jmp") {WIDTH_SUFFIX}?

%%

// Comments

{COMMENT}                              { return AsarTypes.COMMENT_TOKEN; }

// Architecture

{ASAR_VERSION}                          { return AsarTypes.ASAR_VERSION_TOKEN; }

// Labels & constants

{LABEL}                                { return AsarTypes.LABEL_TOKEN; }
{LOCAL_LABEL}                          { return AsarTypes.LOCAL_LABEL_TOKEN; }
{CONSTANT}                             { return AsarTypes.CONSTANT_TOKEN; }
{MACRO_PARAM}                          { return AsarTypes.MACRO_PARAM_TOKEN; }
{NAMESPACED_IDENTIFIER}                { return AsarTypes.IDENTIFIER_TOKEN; }

// Opcodes

{OPCODE}            / {NOT_IDENT_OR_DOT_PART}  { return AsarTypes.OPCODE_TOKEN; }
{OPCODE}                                       { if (isAtEnd()) return AsarTypes.OPCODE_TOKEN; }

{IMPLIED_OPCODE}    / {NOT_IDENT_PART}         { return AsarTypes.IMPLIED_OPCODE_TOKEN; }
{IMPLIED_OPCODE}                               { if (isAtEnd()) return AsarTypes.IMPLIED_OPCODE_TOKEN; }

{BLOCK_MOVE_OPCODE} / {NOT_IDENT_PART}         { return AsarTypes.BLOCK_MOVE_OPCODE_TOKEN; }
{BLOCK_MOVE_OPCODE}                            { if (isAtEnd()) return AsarTypes.BLOCK_MOVE_OPCODE_TOKEN; }

{BRANCH_OPCODE} / {NOT_IDENT_OR_DOT_PART}            { return AsarTypes.BRANCH_OPCODE_TOKEN; }
{BRANCH_OPCODE}                               { if (isAtEnd()) return AsarTypes.BRANCH_OPCODE_TOKEN; }

{JUMP_OPCODE} / {NOT_IDENT_OR_DOT_PART}              { return AsarTypes.JUMP_OPCODE_TOKEN; }
{JUMP_OPCODE}                                 { if (isAtEnd()) return AsarTypes.JUMP_OPCODE_TOKEN; }

// Identifiers

{IDENTIFIER}                           { return AsarTypes.IDENTIFIER_TOKEN; }
{NUMBER_HEX}                           { return AsarTypes.NUMBER_HEX_TOKEN; }
{NUMBER_DEC}                           { return AsarTypes.NUMBER_DEC_TOKEN; }
{NUMBER_BIN}                           { return AsarTypes.NUMBER_BIN_TOKEN; }
{STRING}                               { return AsarTypes.STRING_TOKEN; }

// Multi-Line Support - must be placed before "," and "\"

"\\" [ \t]* {NEWLINE}                  { yybegin(CONTINUATION_BACKSLASH); return TokenType.WHITE_SPACE; }
{COMMA} / [ \t]* {NEWLINE}             { yybegin(COMMA_PENDING_CONTINUATION); return AsarTypes.COMMA_TOKEN; }

<COMMA_PENDING_CONTINUATION> {
    [ \t]* {NEWLINE}                   { yybegin(CONTINUATION_COMMA); return TokenType.WHITE_SPACE; }
}

<CONTINUATION_BACKSLASH> {
    {WHITESPACE}                       { return TokenType.WHITE_SPACE; }
    {COMMENT}                          { return AsarTypes.COMMENT_TOKEN; }
    {NEWLINE}                          { /* skip blank continuation lines */ }
    .                                  { yypushback(1); yybegin(YYINITIAL); }
}

<CONTINUATION_COMMA> {
    {WHITESPACE}                       { return TokenType.WHITE_SPACE; }
    {COMMENT}                          { return AsarTypes.COMMENT_TOKEN; }
    {NEWLINE}                          { /* skip blank continuation lines */ }
    .                                  { yypushback(1); yybegin(YYINITIAL); }
}

// Operators
{PLUS}                                 { return AsarTypes.PLUS_TOKEN; }
{MINUS}                                { return AsarTypes.MINUS_TOKEN; }
{STAR}                                 { return AsarTypes.STAR_TOKEN; }
{SLASH}                                { return AsarTypes.SLASH_TOKEN; }
{PERCENT}                              { return AsarTypes.PERCENT_TOKEN; }
{SHIFT_LEFT}                           { return AsarTypes.SHIFT_LEFT_TOKEN; }
{SHIFT_RIGHT}                          { return AsarTypes.SHIFT_RIGHT_TOKEN; }

{EQ}                                   { return AsarTypes.EQ_TOKEN; }
{NEQ}                                  { return AsarTypes.NEQ_TOKEN; }
{LTE}                                  { return AsarTypes.LTE_TOKEN; }
{GTE}                                  { return AsarTypes.GTE_TOKEN; }
{LT}                                   { return AsarTypes.LT_TOKEN; }
{GT}                                   { return AsarTypes.GT_TOKEN; }

{ASSIGN}                               { return AsarTypes.ASSIGN_TOKEN; }
{PLUS_ASSIGN}                          { return AsarTypes.PLUS_ASSIGN_TOKEN; }
{COLON_ASSIGN}                         { return AsarTypes.COLON_ASSIGN_TOKEN; }
{HASH_ASSIGN}                          { return AsarTypes.HASH_ASSIGN_TOKEN; }
{QUESTION_ASSIGN}                      { return AsarTypes.QUESTION_ASSIGN_TOKEN; }

{COLON}                                { return AsarTypes.COLON_TOKEN; }
{COMMA}                                { return AsarTypes.COMMA_TOKEN; }
{HASH}                                 { return AsarTypes.HASH_TOKEN; }
{LPAREN}                               { return AsarTypes.LPAREN_TOKEN; }
{RPAREN}                               { return AsarTypes.RPAREN_TOKEN; }
{LBRACKET}                             { return AsarTypes.LBRACKET_TOKEN; }
{RBRACKET}                             { return AsarTypes.RBRACKET_TOKEN; }

{LOGICAL_AND}                          { return AsarTypes.LOGICAL_AND_TOKEN; }
{LOGICAL_OR}                           { return AsarTypes.LOGICAL_OR_TOKEN; }
{PIPE}                                 { return AsarTypes.PIPE_TOKEN; }
{AMPERSAND}                            { return AsarTypes.AMPERSAND_TOKEN; }
{CARET}                                { return AsarTypes.CARET_TOKEN; }
{TILDE}                                { return AsarTypes.TILDE_TOKEN; }

// Whitespaces

{WHITESPACE}                           { return TokenType.WHITE_SPACE; }
{NEWLINE}                              { return AsarTypes.NEWLINE_TOKEN; }
.                                      { return TokenType.BAD_CHARACTER; }