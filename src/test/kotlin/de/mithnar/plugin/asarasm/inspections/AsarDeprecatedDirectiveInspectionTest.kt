package de.mithnar.plugin.asarasm.inspections

import de.mithnar.plugin.asarasm.inspection.AsarDeprecatedDirectiveInspection
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AsarDeprecatedDirectiveInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(AsarDeprecatedDirectiveInspection())
    }

    fun testWarnpcIsHighlightedAsDeprecated() {
        myFixture.configureByText("test.asm", "warnpc $00FFFE")
        val highlightInfos = myFixture.doHighlighting()
        val deprecationWarning = highlightInfos.find {
            it.description == "'warnpc' directive is deprecated in 1.9 and will be removed in 2.0"
        }
        assertNotNull("Expected a deprecation highlight on 'warnpc' directive", deprecationWarning)
    }

    fun testQuickFixReplacesWarnpcWithAssertValue() {
        myFixture.configureByText("test.asm", "warnpc $00FFFE")
        val highlightInfos = myFixture.doHighlighting()
        assertTrue(
            "Expected deprecation highlight to be registered",
            highlightInfos.any { it.description == "'warnpc' directive is deprecated in 1.9 and will be removed in 2.0" }
        )

        val intention = myFixture.findSingleIntention("Replace")
        myFixture.launchAction(intention)
        myFixture.checkResult("assert pc() <= $00FFFE")
    }

    fun testQuickFixReplacesWarnpcWithAssertExpression() {
        myFixture.configureByText("test.asm", "warnpc $00FFFE|!SomeConst")
        val highlightInfos = myFixture.doHighlighting()
        assertTrue(
            "Expected deprecation highlight to be registered",
            highlightInfos.any { it.description == "'warnpc' directive is deprecated in 1.9 and will be removed in 2.0" }
        )

        val intention = myFixture.findSingleIntention("Replace")
        myFixture.launchAction(intention)
        myFixture.checkResult("assert pc() <= $00FFFE|!SomeConst")
    }
}