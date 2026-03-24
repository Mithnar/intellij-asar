package de.mithnar.plugin.asarasm

import com.intellij.lexer.FlexAdapter

class AsarLexerAdapter : FlexAdapter(AsarLexer(null))