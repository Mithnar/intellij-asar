package de.mithnar.plugin.asarasm.annotator

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.mithnar.plugin.asarasm.AsarFileType
import de.mithnar.plugin.asarasm.AsarSyntaxHighlighter

class AsarLabelAnnotatorTest : BasePlatformTestCase() {

    private fun highlightedAttributeKeysAt(text: String, keyword: String): List<String> {
        val occurrences = text.windowed(keyword.length).count { it == keyword }
        assert(occurrences == 1) { "Expected exactly one occurrence of '$keyword' in '$text', but found $occurrences" }
        val offset = text.indexOf(keyword)
        myFixture.configureByText(AsarFileType, text)
        val highlights = myFixture.doHighlighting()
        return highlights
            .filter { it.startOffset <= offset && offset < it.endOffset }
            .mapNotNull { it.forcedTextAttributesKey?.externalName }
    }

    fun testLabelDefinition() {
        val code = "Foo:\n  LDA #$00"
        val keys = highlightedAttributeKeysAt(code, "Foo:")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testLocalLabelDefinition() {
        val code = "Foo:\n .Bar\n  LDA #$00"
        val keys = highlightedAttributeKeysAt(code, ".Bar")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testSymbolReferenceTarget() {
        val code = "  BRA Foo"
        val keys = highlightedAttributeKeysAt(code, "Foo")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testConstantIsNotLabel() {
        val code = "  LDA !MY_FOOBAR"
        val keys = highlightedAttributeKeysAt(code, "!MY_FOOBAR")
        assertDoesntContain(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testLabelReferenceInOperant() {
        val code = "  LDA Foo,x"
        val keys = highlightedAttributeKeysAt(code, "Foo")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testAnonymousMinusLabelDefinition() {
        val code = "-\n  LDA #$01\n  BRA +"
        val keys = highlightedAttributeKeysAt(code, "-")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testAnonymousPlusPlusLabelDefinition() {
        val code = "++\n  LDA #$01\n  BRA --"
        val keys = highlightedAttributeKeysAt(code, "++")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testAnonymousMinusLabelReference() {
        val code = "LDA #$01\n  BRA -"
        val keys = highlightedAttributeKeysAt(code, "-")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }

    fun testAnonymousPlusPlusLabelReference() {
        val code = "LDA #$01\n  BRA ++"
        val keys = highlightedAttributeKeysAt(code, "++")
        assertContainsElements(keys, AsarSyntaxHighlighter.LABEL.externalName)
    }
}