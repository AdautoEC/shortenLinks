package br.com.warehouse.shortenlinks.core.di

import br.com.warehouse.shortenlinks.core.validation.UrlValidator
import br.com.warehouse.shortenlinks.core.validation.UrlValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for providing validation-related dependencies.
 */
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class ValidationModule {

    /**
     * Binds the [UrlValidatorImpl] to the [UrlValidator] interface.
     */
    @Binds
    abstract fun bindUrlValidator(impl: UrlValidatorImpl): UrlValidator
}
