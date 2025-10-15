package br.com.warehouse.shortenlinks.features.shorten.di

import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import br.com.warehouse.shortenlinks.features.shorten.data.repository.ShortenRepositoryImpl
import br.com.warehouse.shortenlinks.features.shorten.data.service.ShortenService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies for the shorten feature.
 */
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class ShortenModule {

    /**
     * Binds the [ShortenRepositoryImpl] to the [ShortenRepository] interface.
     */
    @Binds
    abstract fun bindShortenRepository(impl: ShortenRepositoryImpl): ShortenRepository

    companion object {
        /**
         * Provides the [ShortenService] instance.
         */
        @Provides
        @Singleton
        fun provideShortenService(retrofit: Retrofit): ShortenService {
            return retrofit.create(ShortenService::class.java)
        }
    }
}