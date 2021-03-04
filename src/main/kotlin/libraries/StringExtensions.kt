package libraries

fun String.findSubstringCount(subString: String): Int = this.windowed(subString.length).count { it == subString }
