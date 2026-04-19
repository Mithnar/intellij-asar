package de.mithnar.plugin.asarasm.inspection.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import de.mithnar.plugin.asarasm.inspection.AsarDeprecatedDirectiveInspection
import de.mithnar.plugin.asarasm.psi.AsarAssertDirective
import de.mithnar.plugin.asarasm.psi.AsarTypes.IDENTIFIER_TOKEN
import de.mithnar.plugin.asarasm.psi.AsarTypes.NUMBER_HEX_TOKEN
import de.mithnar.plugin.asarasm.psi.AsarWarnpcDirective

class ReplaceWarnpcWithAssertFix : LocalQuickFix {

    override fun getName(): String = "Replace with 'assert pc() <= ...'"

    override fun getFamilyName(): String = AsarDeprecatedDirectiveInspection.Constants.FIXES_FAMILY_NAME

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val psiElement = descriptor.psiElement as? AsarWarnpcDirective ?: return
        val addressText = extractExpression(psiElement) ?: return

        val replacementText = "assert pc() <= $addressText"
        val newElement = createAssertDirective(project, psiElement, replacementText) ?: return
        psiElement.replace(newElement)
    }

    private fun extractExpression(warnpcDirective: AsarWarnpcDirective): String? {
        val keywordNode = warnpcDirective.node.findChildByType(IDENTIFIER_TOKEN) ?: return null
        val afterKeyword = keywordNode.psi.nextSibling ?: return null

        // Collect all text after the keyword
        val sb = StringBuilder()
        var current: PsiElement? = afterKeyword
        while (current != null) {
            sb.append(current.text)
            current = current.nextSibling
        }
        val result = sb.toString().trim()
        return result.ifEmpty { null }
    }

    private fun createAssertDirective(project: Project, context: PsiElement, text: String): PsiElement? {
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.asm", context.containingFile.fileType, text)
        return PsiTreeUtil.findChildOfType(file, AsarAssertDirective::class.java)
    }
}