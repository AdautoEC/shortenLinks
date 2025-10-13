package br.com.warehouse.shortenlinks.di

import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideShortenUrlUseCase(): ShortenUrlUseCase = mockk(relaxed = true)
}
