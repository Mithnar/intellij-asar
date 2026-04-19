package de.mithnar.plugin.asarasm.annotator

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.mithnar.plugin.asarasm.AsarFileType

class AsarSa1RomAnnotatorTest : BasePlatformTestCase() {

    private fun errorMessages(text: String): List<String> {
        myFixture.configureByText(AsarFileType, text)
        return myFixture.doHighlighting()
            .filter { it.severity.name == "ERROR" }
            .mapNotNull { it.description }
    }

    private fun errorRanges(text: String): List<String> {
        myFixture.configureByText(AsarFileType, text)
        return myFixture.doHighlighting()
            .filter { it.severity.name == "ERROR" }
            .map { text.substring(it.startOffset, it.endOffset) }
    }

    fun testValidSa1romAllZeros() {
        val errors = errorMessages("sa1rom 0,0,0,0")
        assertEmpty(errors)
    }

    fun testValidSa1romAllSevens() {
        val errors = errorMessages("sa1rom 7,7,7,7")
        assertEmpty(errors)
    }

    fun testValidSa1romMixedValues() {
        val errors = errorMessages("sa1rom 0,1,6,7")
        assertEmpty(errors)
    }

    fun testValidSa1romWithoutArgs() {
        val errors = errorMessages("sa1rom")
        assertEmpty(errors)
    }

    fun testInvalidSa1romValueEight() {
        val errors = errorMessages("sa1rom 8,0,0,0")
        assertContainsElements(errors, "Bank values for sa1rom must be between 0 and 7")
    }

    fun testInvalidSa1romLargeNumber() {
        val errors = errorMessages("sa1rom 0,0,0,255")
        assertContainsElements(errors, "Bank values for sa1rom must be between 0 and 7")
    }

    fun testInvalidSa1romMultipleOutOfRange() {
        val errors = errorMessages("sa1rom 8,9,10,11")
        assertEquals(4, errors.size)
    }

    fun testInvalidSa1romHighlightsCorrectToken() {
        val ranges = errorRanges("sa1rom 0,9,0,0")
        assertContainsElements(ranges, "9")
        assertEquals(1, ranges.size)
    }
}