package de.mithnar.plugin.asarasm.reference

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import java.nio.file.Path

class AsarIncludePsiReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    override fun resolve(): PsiElement? {
        val definedPath = rangeInElement.substring(element.text)
        val containingVirtualFile = element.containingFile.originalFile.virtualFile ?: return null
        val parentDir = containingVirtualFile.parent ?: return null

        val targetVirtualFile = if (definedPath.startsWith("/")) {
            val fileSystem = containingVirtualFile.fileSystem
            fileSystem.findFileByPath(definedPath)
        } else {
            val normalizedPath = Path.of(definedPath).normalize().toString()
            VfsUtil.findRelativeFile(parentDir, *normalizedPath.split("/").toTypedArray())
        }

        targetVirtualFile ?: return null
        return PsiManager.getInstance(element.project).findFile(targetVirtualFile)
    }
}