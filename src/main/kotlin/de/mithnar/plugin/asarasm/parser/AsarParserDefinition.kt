package de.mithnar.plugin.asarasm.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import de.mithnar.plugin.asarasm.AsarLanguage
import de.mithnar.plugin.asarasm.AsarLexerAdapter
import de.mithnar.plugin.asarasm.psi.AsarFile
import de.mithnar.plugin.asarasm.psi.AsarTypes

class AsarParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = AsarLexerAdapter()
    override fun createParser(project: Project): PsiParser = AsarParser()
    override fun getFileNodeType(): IFileElementType = IFileElementType(AsarLanguage)
    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
    override fun getCommentTokens(): TokenSet = TokenSet.create(AsarTypes.COMMENT_TOKEN)
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY
    override fun createElement(node: ASTNode): PsiElement = AsarTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider): PsiFile = AsarFile(viewProvider)
}