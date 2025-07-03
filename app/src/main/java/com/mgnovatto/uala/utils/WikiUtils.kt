package com.mgnovatto.uala.utils

object WikiUtils {

    fun isValidDescription(extract: String?): Boolean {
        if (extract.isNullOrBlank()) {
            return false
        }

        val isDisambiguation = extract.contains("may refer to:", ignoreCase = true) ||
                extract.contains("puede referirse a", ignoreCase = true)

        return !isDisambiguation
    }

    fun cleanWikipediaExtract(extract: String?): String? {
        if (extract == null) return null

        val citationRegex = "\\[\\d+]".toRegex()

        return extract
            .replace(citationRegex, " ")
            .trim()
    }
}