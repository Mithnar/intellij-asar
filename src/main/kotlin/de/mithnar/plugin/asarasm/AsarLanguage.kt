package de.mithnar.plugin.asarasm

import com.intellij.lang.Language

object AsarLanguage : Language("AsarAsm") {
    private fun readResolve(): Any = AsarLanguage
}