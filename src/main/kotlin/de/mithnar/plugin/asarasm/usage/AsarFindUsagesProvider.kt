package de.mithnar.plugin.asarasm.usage

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import de.mithnar.plugin.asarasm.AsarLexerAdapter
import de.mithnar.plugin.asarasm.psi.*

class AsarFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            AsarLexerAdapter(),
            TokenSet.create(
                AsarTypes.LABEL_TOKEN,
                AsarTypes.LOCAL_LABEL_TOKEN,
                AsarTypes.IDENTIFIER_TOKEN,
                AsarTypes.CONSTANT_TOKEN
            ),
            TokenSet.create(AsarTypes.COMMENT_TOKEN),
            TokenSet.create(AsarTypes.NUMBER_TOKEN, AsarTypes.STRING_TOKEN),
        )
    }

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        return element is AsarLabelDefinition
    }

    override fun getHelpId(element: PsiElement): String? = null

    override fun getType(element: PsiElement): String = when (element) {
        is AsarLabelDefinition -> "label"
        is AsarAnonymousLabelDefinition -> "anonymous label"
        is AsarLocalLabelDefinition -> "local label"
        is AsarConstantDefinition -> "constant"
        else -> "unknown"
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return (element as? PsiNamedElement)?.name ?: element.text
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return (element as? PsiNamedElement)?.name ?: element.text
    }
}