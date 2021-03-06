package libraries

fun String.findSubstringCount(subString: String): Int {
    if (subString.isEmpty()) return 0

    return this.windowed(subString.length).count { it == subString }
}
