package de.mithnar.plugin.asarasm.parser

import com.intellij.psi.PsiFile
import com.intellij.testFramework.ParsingTestCase

class AsarParserLabelTest : ParsingTestCase("input/label", "asm", AsarParserDefinition()) {
    override fun getTestDataPath(): String = "src/test/resources/parser"

    override fun checkResult(targetDataName: String, file: PsiFile) {
        checkResult(getTestDataPath() + "/expectation/label", targetDataName, file);
    }

    fun testGlobalMainLabelInMacro() {
        doTest(true)
    }

    fun testMainLabels() {
        doTest(true)
    }

    fun testMainLabels_Assignment() {
        doTest(true)
    }

    fun testMainLabels_Basic() {
        doTest(true)
    }

    fun testSubLabels() {
        doTest(true)
    }

    fun testSubLabels_Nested() {
        doTest(true)
    }

    fun testLocalLabels() {
        doTest(true)
    }

    fun testLocalLabels_Chains() {
        doTest(true)
    }

    fun testLabels_HashPrefix() {
        doTest(true)
    }

    fun testLabels_Mixed_EdgeCases() {
        doTest(true)
    }

    fun testMacroLabels() {
        doTest(true)
    }

    fun testMacroLabels_HashPrefixed() {
        doTest(true)
    }

    fun testMacroLabels_Assignment() {
        doTest(true)
    }

    fun testMacroLabels_AnonymousDepth() {
        doTest(true)
    }

    fun testMacroLabels_BranchAndJump() {
        doTest(true)
    }

    fun testMacroLabels_InArithmetic() {
        doTest(true)
    }

    fun testMacroLabels_Negative_WhitespaceAfterQuestion() {
        doTest(true)
    }
}