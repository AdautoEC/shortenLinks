package br.com.warehouse.shortenlinks.core.validation

import android.util.Patterns
import javax.inject.Inject

class UrlValidatorImpl @Inject constructor() : UrlValidator {
    override fun isValid(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }
}
