package de.mithnar.plugin.asarasm.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import de.mithnar.plugin.asarasm.AsarFileType
import de.mithnar.plugin.asarasm.AsarLanguage

class AsarFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AsarLanguage) {
    override fun getFileType(): FileType = AsarFileType
    override fun toString(): String = "Asar File"
}