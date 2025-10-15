package br.com.warehouse.shortenlinks.core.validation

import android.util.Patterns
import javax.inject.Inject

/**
 * A default implementation of [UrlValidator] that uses the [Patterns.WEB_URL] pattern to validate URLs.
 */
class UrlValidatorImpl @Inject constructor() : UrlValidator {

    /**
     * Checks if the given [url] is a valid URL.
     *
     * @param url The URL to validate.
     * @return `true` if the URL is valid, `false` otherwise.
     */
    override fun isValid(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }
}
