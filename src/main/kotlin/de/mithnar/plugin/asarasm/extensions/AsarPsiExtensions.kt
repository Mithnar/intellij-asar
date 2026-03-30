package de.mithnar.plugin.asarasm.extensions

import de.mithnar.plugin.asarasm.annotator.AddressingMode
import de.mithnar.plugin.asarasm.annotator.OpcodeAddressingModes.REPEAT_OPCODES
import de.mithnar.plugin.asarasm.psi.AsarIndexRegister
import de.mithnar.plugin.asarasm.psi.AsarOperand
import de.mithnar.plugin.asarasm.psi.AsarTypes

fun AsarIndexRegister.isX(): Boolean =
    firstChild?.node?.elementType == AsarTypes.IDENTIFIER_TOKEN &&
            firstChild?.text.equals("x", ignoreCase = true)

fun AsarIndexRegister.isY(): Boolean =
    firstChild?.node?.elementType == AsarTypes.IDENTIFIER_TOKEN &&
            firstChild?.text.equals("y", ignoreCase = true)

fun AsarIndexRegister.isStack(): Boolean =
    firstChild?.node?.elementType == AsarTypes.IDENTIFIER_TOKEN &&
            firstChild?.text.equals("s", ignoreCase = true)

fun AsarOperand.addressingMode(opcodeText: String): AddressingMode = when {
    hashOperand != null && opcodeText in REPEAT_OPCODES -> AddressingMode.REPEAT_COUNT
    hashOperand != null -> AddressingMode.IMMEDIATE
    stackRelativeIndirectIndexedMode != null -> AddressingMode.STACK_RELATIVE_INDIRECT_INDEXED
    indexedIndirectMode != null -> AddressingMode.INDEXED_INDIRECT
    indirectIndexedOrIndirectMode?.indexRegister != null -> AddressingMode.INDIRECT_INDEXED
    indirectIndexedOrIndirectMode != null -> AddressingMode.INDIRECT
    longIndirectIndexedMode?.indexRegister != null -> AddressingMode.LONG_INDIRECT_INDEXED
    longIndirectIndexedMode != null -> AddressingMode.LONG_INDIRECT
    accumulatorOperand != null -> AddressingMode.ACCUMULATOR
    indexedMode?.indexRegister?.isStack() == true -> AddressingMode.STACK_RELATIVE
    indexedMode?.indexRegister?.isX() == true -> AddressingMode.ABSOLUTE_X
    indexedMode?.indexRegister?.isY() == true -> AddressingMode.ABSOLUTE_Y
    else -> AddressingMode.ABSOLUTE
}