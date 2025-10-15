package br.com.warehouse.shortenlinks.core.validation

interface UrlValidator {
    fun isValid(url: String): Boolean
}
