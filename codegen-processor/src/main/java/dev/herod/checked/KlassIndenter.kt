package dev.herod.checked

object KlassIndenter {

    fun indent(input: String): String {
        return input
                .trimIndent()
                .lines()
                .reduce { acc, s ->
                    acc.trim() + "\n" + s.trim()
                }.lines()
                .map { it.trim() }
                .reduce(::eachLine)
                .lines()
                .map { it.replace("\t", "".padStart(4, ' ')) }
                .map { it.replace("\\s{4}".toRegex(), "\t") }
                .map { it.replace("\\s*//\\d+".toRegex(), "") }
                .reduce { acc, s -> "$acc\n$s" }
    }

    private fun eachLine(acc: String, s: String): String {
        val prev = acc.trim().substringAfterLast("//").toIntOrNull() ?: 0
        val add = s.count { it == '{' || it == '(' }
        val sub = s.count { it == '}' || it == ')' }
        val next = prev + add - sub
        val pad = (if (add > sub) prev else next) + if (s.startsWith("get()")) 1 else 0
        return "$acc\n${"".padStart(maxOf(pad, 0), '\t')}$s"
                .let { if (next < prev) "$it\n" else it }
                .let { "$it //$next" }
    }
}