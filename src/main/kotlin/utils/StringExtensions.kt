package utils

/**
 * String extension to find the number of occurrences of substring.
 * @param[subString] the substring whose number of occurrences is being search for.
 * @return the number of occurrences of substring.
 */
fun String.findSubstringCount(subString: String): Int {
    if (subString.isEmpty()) return 0

    return this.windowed(subString.length).count { it == subString }
}
