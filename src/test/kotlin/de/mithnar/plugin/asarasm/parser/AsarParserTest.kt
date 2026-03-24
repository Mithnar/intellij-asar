package de.mithnar.plugin.asarasm.parser

import com.intellij.testFramework.ParsingTestCase
import de.mithnar.plugin.asarasm.parser.AsarParserDefinition

class AsarParserTest : ParsingTestCase("", "asm", AsarParserDefinition()) {

    override fun getTestDataPath(): String = "src/test/resources/parser"

    fun testLongIndirectIndexed() {
        doTest(true)
    }

    fun testLongIndirectPlain() {
        doTest(true)
    }

    fun testBracketExpressionConflict() {
        doTest(true)
    }

    fun testMalformedOperand() {
        doTest(true)
    }

    fun testMalformedBranchTarget() {
        doTest(true)
    }

    fun testIndexedOperand() {
        doTest(true)
    }

    fun testIndirectIndexedOperand() {
        doTest(true)
    }

    fun testIndexedIndirectOperand() {
        doTest(true)
    }

    fun testLabelThenDirective() {
        doTest(true)
    }

    fun testLabelThenInstruction() {
        doTest(true)
    }

    fun testPrintDirectiveWithPc() {
        doTest(true)
    }

    fun testMultiLineDirectives() {
        doTest(true)
    }

    fun testLabelDefinitionAndLabelJump() {
        doTest(true)
    }

    fun testAnonymousLabelBranchForward() {
        doTest(true)
    }

    fun testBranchToLocalLabel() {
        doTest(true)
    }

    fun testWidthOverride() {
        doTest(true)
    }

    fun testBinaryLiterals() {
        doTest(true)
    }

    fun testComparisonDoubleSymbolOperators() {
        doTest(true)
    }

    fun testRepeatShorthand() {
        doTest(true)
    }

    fun testLocalLabelInPrimaryExpression() {
        doTest(true)
    }

    fun testJumpTargetAddressingModes() {
        doTest(true)
    }

    fun testImpliedInstuctionsSupportRepeat() {
        doTest(true)
    }

    fun testAssertWithLogicalOperators() {
        doTest(true)
    }

    fun testComplexConstantReassign(){
        doTest(true)
    }

    fun testConstantAssignFunction(){
        doTest(true)
    }

    fun testFunctionDefinition(){
        doTest(true)
    }

    fun testBranchToExpression(){
        doTest(true)
    }

    fun testBranchAndJumpWithWidthSuffix(){
        doTest(true)
    }

    fun testAsarVersionDirective() {
        doTest(true)
    }

    fun testFreespaceDirectives() {
        doTest(true)
    }

    fun testAutocleanDirective() {
        doTest(true)
    }

    fun testProtDirective() {
        doTest(true)
    }
}