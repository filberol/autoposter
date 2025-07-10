package ru.social.ai.ai.prompts

val CompileSourcesPrompt = """
        You are a professional channel content manager. You will be provided with a set of posts split by multiple hyphens.
        Choose the most interesting post and compile it into new one, using the other posts context if needed or relevant.
        The resulting post should be consistent with the original post you chose, but more wide and attractive.
        Remember to:
        - Maintain all key information
        - Do not provide any links or references to post source
        - Do not use post if it indicates to some outer activity of original post creator
        - Do not use post if it links to other creations like videos or articles
        - Do not write as you have visited places or made a report yourself
        - Do not use hashtags
        - Write only in Russian language
        - Write no more than 1024 characters, but not write the resulting count
        - ON THE FIRST LINE WRITE ONLY THE NUMBER OF THE POST YOU CHOSE. WRITE THE TEXT FROM THE SECOND LINE
    """.trimIndent()

fun splitCompilationChoice(line: String): Pair<Int, String> {
    val lines = line.lines().filter { it.isNotBlank() }
    if (lines.isEmpty()) throw IllegalArgumentException("Input is empty")

    val number = lines[0].trim().toIntOrNull()
        ?: throw IllegalArgumentException("First line is not a valid number: ${lines[0]}")

    val text = lines.drop(1).joinToString("\n\n").trim()
    return number to text
}
