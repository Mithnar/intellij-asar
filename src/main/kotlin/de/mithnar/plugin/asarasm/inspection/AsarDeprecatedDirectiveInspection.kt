package de.mithnar.plugin.asarasm.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import de.mithnar.plugin.asarasm.inspection.quickfix.ReplaceWarnpcWithAssertFix
import de.mithnar.plugin.asarasm.psi.AsarWarnpcDirective

class AsarDeprecatedDirectiveInspection : LocalInspectionTool() {
    object Constants {
        const val FIXES_FAMILY_NAME = "Asar deprecated directive fixes"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is AsarWarnpcDirective) {
                    holder.registerProblem(
                        element,
                        "'warnpc' directive is deprecated in 1.9 and will be removed in 2.0",
                        ProblemHighlightType.LIKE_DEPRECATED,
                        ReplaceWarnpcWithAssertFix()
                    )
                }
            }
        }
    }
}