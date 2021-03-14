package libraries

/**
 * String extension to find the number of occurrences of substring.
 * @param[subString] the substring whose number of occurrences is being search for.
 * @return the number of occurrences of substring.
 */
fun String.findSubstringCount(subString: String) : Int {
    if (subString.isEmpty()) return 0

    return this.windowed(subString.length).count { it == subString }
}

/**
 * String extension to parse integer numbers.
 * If found element is not a number, it is skipped.
 * @param[splitString] the string used to split the numbers.
 * @return the list of parsed numbers.
 */
fun String.parseIntegers(splitString: String = " ") : List<Long> {
    return this.split(splitString).mapNotNull { it.toLongOrNull() }
}
