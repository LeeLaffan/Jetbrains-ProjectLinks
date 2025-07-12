package lee.jetbrains.projectlinks;

internal class LinkFinder {
    internal fun getLinks(readmeLines: List<String>): List<Pair<String, String>> {
        val links = mutableListOf<Pair<String, String>>()

        for (line in readmeLines) {
            val result: Pair<String, String>? = getLinkFromLine(line)
            if (result != null) {
                links.add(result)
            }
        }

        return links.toList()
    }

    private fun getLinkFromLine(line: String): Pair<String, String>? {
        val openBracket = line.indexOf('[')
        // Skip image links (those prefixed with '!' character)
        if (openBracket > 0 && line[openBracket - 1] == '!') {
            return null
        }
        val closeBracket = line.indexOf(']', openBracket + 1)
        if (openBracket == -1 || closeBracket == -1 || closeBracket + 1 >= line.length || line[closeBracket + 1] != '(')
            return null

        val text = line.substring(openBracket + 1, closeBracket).trim()

        val openParen = closeBracket + 1
        val startUrl = openParen + 1
        var depth = 1
        var endUrl = -1

        for (i in startUrl until line.length) {
            when (line[i]) {
                '(' -> depth++
                ')' -> {
                    depth--
                    if (depth == 0) {
                        endUrl = i
                        break
                    }
                }
            }
        }

        if (endUrl == -1) return null

        val url = line.substring(startUrl, endUrl).trim()
        return text to url
    }
}
