package de.mithnar.plugin.asarasm.reference

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AsarIncludePsiReferenceTest : BasePlatformTestCase() {

    fun testIncsrcUnquotedRelativeReference() {
        myFixture.addFileToProject("included.asm", "; contents")
        myFixture.configureByText("test.asm", "incsrc included.asm")
        assertResolvesTo("/src", "included.asm", "incsrc")
    }

    fun testIncsrcQuotedRelativeReference() {
        myFixture.addFileToProject("included.asm", "; contents")
        myFixture.configureByText("test.asm", "incsrc \"included.asm\"")
        assertResolvesTo("/src", "included.asm", "incsrc")
    }

    fun testIncsrcQuotedWithSpacesReference() {
        myFixture.addFileToProject("routine with spaces.asm", "; contents")
        myFixture.configureByText("test.asm", "incsrc \"routine with spaces.asm\"")
        assertResolvesTo("/src", "routine with spaces.asm", "incsrc")
    }

    fun testIncsrcSubdirectoryReference() {
        myFixture.addFileToProject("sub/sub.asm", "; contents")
        myFixture.configureByText("test.asm", "incsrc \"sub/sub.asm\"")
        assertResolvesTo("/src/sub", "sub.asm", "incsrc")
    }

    fun testIncsrcParentTraversalReference() {
        myFixture.addFileToProject("routine.asm", "; contents")
        myFixture.configureByText("test.asm", "incsrc \"sub/../routine.asm\"")
        assertResolvesTo("/src", "routine.asm", "incsrc")
    }

    fun testIncsrcAbsolutePathReference() {
        val file = myFixture.addFileToProject("sub/sub.asm", "; contents")
        val absolutePath = file.virtualFile.path
        myFixture.configureByText("test.asm", "incsrc \"$absolutePath\"")
        assertResolvesTo("/src/sub", "sub.asm", "incsrc")
    }

    fun testIncsrcHigherLevelPathReference() {
        myFixture.addFileToProject("other.asm", "; contents")
        val test = myFixture.addFileToProject("sub/test.asm", "incsrc \"../other.asm\"")
        myFixture.configureFromExistingVirtualFile(test.virtualFile)
        assertResolvesTo("/src", "other.asm", "incsrc")
    }

    fun testIncsrcfromHigherLevelPathReference() {
        myFixture.addFileToProject("routine.asm", "; contents")
        val test = myFixture.addFileToProject("sub/test.asm", "incsrc \"../routine.asm\"")
        myFixture.configureFromExistingVirtualFile(test.virtualFile)
        assertResolvesTo("/src", "routine.asm", "includefrom")
    }

    fun testIncludefromUnquotedRelativeReference() {
        myFixture.addFileToProject("includer.asm", "; contents")
        myFixture.configureByText("test.asm", "includefrom includer.asm")
        assertResolvesTo("/src", "includer.asm", "includefrom")
    }

    fun testIncludefromQuotedRelativeReference() {
        myFixture.addFileToProject("includer.asm", "; contents")
        myFixture.configureByText("test.asm", "includefrom \"includer.asm\"")
        assertResolvesTo("/src", "includer.asm", "includefrom")
    }

    fun testIncludefromQuotedWithSpacesReference() {
        myFixture.addFileToProject("routine with spaces.asm", "; contents")
        myFixture.configureByText("test.asm", "includefrom \"routine with spaces.asm\"")
        assertResolvesTo("/src", "routine with spaces.asm", "includefrom")
    }

    fun testIncludefromSubdirectoryReference() {
        myFixture.addFileToProject("sub/sub.asm", "; contents")
        myFixture.configureByText("test.asm", "includefrom \"sub/sub.asm\"")
        assertResolvesTo("/src/sub", "sub.asm", "includefrom")
    }

    fun testIncludefromParentTraversalReference() {
        myFixture.addFileToProject("routine.asm", "; contents")
        myFixture.configureByText("test.asm", "includefrom \"sub/../routine.asm\"")
        assertResolvesTo("/src", "routine.asm", "includefrom")
    }

    fun testIncludefromAbsolutePathReference() {
        val file = myFixture.addFileToProject("sub/sub.asm", "; contents")
        val absolutePath = file.virtualFile.path
        myFixture.configureByText("test.asm", "includefrom \"$absolutePath\"")
        assertResolvesTo("/src/sub", "sub.asm", "includefrom")
    }

    fun testIncludefromHigherLevelPathReference() {
        myFixture.addFileToProject("routine.asm", "; contents")
        val test = myFixture.addFileToProject("sub/test.asm", "includefrom \"../routine.asm\"")
        myFixture.configureFromExistingVirtualFile(test.virtualFile)
        assertResolvesTo("/src", "routine.asm", "includefrom")
    }

    private fun assertResolvesTo(expectedDirectory: String, expectedFileName: String, directive: String) {
        val text = myFixture.file.text
        val pathStart = text.indexOf("$directive ") + "$directive ".length
        val actualStart = if (text[pathStart] == '"') pathStart + 1 else pathStart

        val reference = myFixture.file.findReferenceAt(actualStart)
        assertNotNull("Reference should not be null", reference)
        val resolved = reference?.resolve()
        assertNotNull("Reference should resolve", resolved)
        assertEquals(expectedFileName, resolved?.containingFile?.name)
        assertEquals("PsiDirectory:$expectedDirectory", resolved?.containingFile?.containingDirectory.toString())
    }
}