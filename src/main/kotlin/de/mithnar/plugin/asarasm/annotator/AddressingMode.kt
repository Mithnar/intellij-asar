package de.mithnar.plugin.asarasm.annotator

enum class AddressingMode(val displayName: String) {
    IMMEDIATE("immediate (#)"),
    ABSOLUTE("absolute / dp / long"),
    ABSOLUTE_X("absolute,X / dp,X"),
    ABSOLUTE_Y("absolute,Y / dp,Y"),
    INDEXED_INDIRECT("(dp,X)"),
    INDIRECT("(dp)"),
    INDIRECT_INDEXED("(dp),Y"),
    LONG_INDIRECT("[dp]"),
    LONG_INDIRECT_INDEXED("[dp],Y"),
    ACCUMULATOR("accumulator (A)"),
    STACK_RELATIVE("dp,S"),
    STACK_RELATIVE_INDIRECT_INDEXED("(dp,S),Y"),

    REPEAT_COUNT("repeat count (#n)") // Not really an AddressingMode, but the implementation fits cleanly
}