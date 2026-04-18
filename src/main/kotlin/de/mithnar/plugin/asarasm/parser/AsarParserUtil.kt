package de.mithnar.plugin.asarasm.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import de.mithnar.plugin.asarasm.psi.AsarTypes

class AsarParserUtil : GeneratedParserUtilBase() {

    companion object {

        private fun matchKeyword(builder: PsiBuilder, keyword: String): Boolean {
            if (builder.tokenType != AsarTypes.IDENTIFIER_TOKEN) return false
            if (!builder.tokenText.equals(keyword, ignoreCase = true)) return false
            builder.advanceLexer()
            return true
        }

        // Seperate functions, because String keyword in parser generator generates the value as symbol, not as string.
        // Boilerplate-y workaround for now until I have a better solution
        // For use in grammar for matching IdentifierTokens with specific (in this case directives) keywords
        @JvmStatic
        fun kw_a(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "a")

        @JvmStatic
        fun kw_x(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "x")

        @JvmStatic
        fun kw_y(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "y")

        @JvmStatic
        fun kw_s(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "s")

        @JvmStatic
        fun kw_pc(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "pc")

        @JvmStatic
        fun kw_asar(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "asar")

        @JvmStatic
        fun kw_namespace(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "namespace")

        @JvmStatic
        fun kw_nested(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "nested")

        @JvmStatic
        fun kw_on(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "on")

        @JvmStatic
        fun kw_off(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "off")

        @JvmStatic
        fun kw_db(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "db")

        @JvmStatic
        fun kw_dw(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "dw")

        @JvmStatic
        fun kw_dl(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "dl")

        @JvmStatic
        fun kw_dd(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "dd")

        @JvmStatic
        fun kw_org(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "org")

        @JvmStatic
        fun kw_assert(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "assert")

        @JvmStatic
        fun kw_incbin(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "incbin")

        @JvmStatic
        fun kw_incsrc(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "incsrc")

        @JvmStatic
        fun kw_include(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "include")

        @JvmStatic
        fun kw_includeonce(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "includeonce")

        @JvmStatic
        fun kw_includefrom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "includefrom")

        @JvmStatic
        fun kw_print(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "print")

        @JvmStatic
        fun kw_error(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "error")

        @JvmStatic
        fun kw_lorom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "lorom")

        @JvmStatic
        fun kw_hirom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "hirom")

        @JvmStatic
        fun kw_sa1rom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "sa1rom")

        @JvmStatic
        fun kw_norom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "norom")

        @JvmStatic
        fun kw_exlorom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "exlorom")

        @JvmStatic
        fun kw_exhirom(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "exhirom")

        @JvmStatic
        fun kw_header(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "header")

        @JvmStatic
        fun kw_noheader(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "noheader")

        @JvmStatic
        fun kw_pushpc(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "pushpc")

        @JvmStatic
        fun kw_pullpc(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "pullpc")

        @JvmStatic
        fun kw_base(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "base")

        @JvmStatic
        fun kw_warnpc(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "warnpc")

        @JvmStatic
        fun kw_freespace(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "freespace")

        @JvmStatic
        fun kw_freecode(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "freecode")

        @JvmStatic
        fun kw_freedata(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "freedata")

        @JvmStatic
        fun kw_freespacebyte(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "freespacebyte")

        @JvmStatic
        fun kw_autoclean(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "autoclean")

        @JvmStatic
        fun kw_prot(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "prot")

        @JvmStatic
        fun kw_table(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "table")

        @JvmStatic
        fun kw_cleartable(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "cleartable")

        @JvmStatic
        fun kw_padbyte(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "padbyte")

        @JvmStatic
        fun kw_pad(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "pad")

        @JvmStatic
        fun kw_fillbyte(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "fillbyte")

        @JvmStatic
        fun kw_fill(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "fill")

        @JvmStatic
        fun kw_arch(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "arch")

        @JvmStatic
        fun kw_math(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "math")

        @JvmStatic
        fun kw_check(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "check")

        @JvmStatic
        fun kw_macro(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "macro")

        @JvmStatic
        fun kw_endmacro(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "endmacro")

        @JvmStatic
        fun kw_function(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "function")

        @JvmStatic
        fun kw_if(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "if")

        @JvmStatic
        fun kw_elseif(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "elseif")

        @JvmStatic
        fun kw_else(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "else")

        @JvmStatic
        fun kw_endif(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "endif")

        @JvmStatic
        fun kw_while(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "while")

        @JvmStatic
        fun kw_endwhile(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "endwhile")

        @JvmStatic
        fun kw_skip(builder: PsiBuilder, level: Int): Boolean = matchKeyword(builder, "skip")
    }
}