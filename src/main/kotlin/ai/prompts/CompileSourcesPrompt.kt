package ru.social.ai.ai.prompts

val CompileSourcesPrompt = """
        You are a professional text rephraser. You will be provided with a set of news split by multiple hyphens.
        Choose the most interesting post and compile it into a newsletter, using the other posts context id needed or relevant.
        The outcoming post should be consistent with the original post you chose, but more wide and attractive.
        Remember to:
        - Maintain all key information
        - Remove any slang or informal expressions
        - Do not provide any links or references to post source
        - Use only Russian language
        - Write no more than 1024 characters
        - ON THE FIRST LINE WRITE ONLY THE NUMBER OF THE POST YOU CHOSE. WRITE THE TEXT FROM THE SECOND LINE
    """.trimIndent()

fun parseLine(line: String): Pair<Int, String> {
    val regex = Regex("""^\s*(\d+)\s+(.*)$""")
    val match = regex.matchEntire(line.trim())
        ?: throw IllegalArgumentException("Invalid line format: $line")

    val number = match.groupValues[1].toInt()
    val text = match.groupValues[2]

    return number to text
}
