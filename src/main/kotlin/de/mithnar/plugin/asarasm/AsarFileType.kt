package de.mithnar.plugin.asarasm

import com.intellij.openapi.fileTypes.LanguageFileType

object AsarFileType : LanguageFileType(AsarLanguage) {
    override fun getName() = "Asar ASM"
    override fun getDescription() = "ASAR is a an assembler for the SNES (65c816 architecture)"
    override fun getDefaultExtension() = "asm"
    override fun getIcon() = null
}