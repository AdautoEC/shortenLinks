package br.com.warehouse.shortenlinks.core.validation

/**
 * An interface for validating URLs.
 */
interface UrlValidator {
    /**
     * Checks if the given [url] is a valid URL.
     *
     * @param url The URL to validate.
     * @return `true` if the URL is valid, `false` otherwise.
     */
    fun isValid(url: String): Boolean
}
