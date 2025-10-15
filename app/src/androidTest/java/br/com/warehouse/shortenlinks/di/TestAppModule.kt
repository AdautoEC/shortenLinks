package br.com.warehouse.shortenlinks.di

import br.com.warehouse.shortenlinks.features.shorten.data.repository.FakeShortenRepository
import br.com.warehouse.shortenlinks.features.shorten.di.ShortenModule
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ShortenModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideShortenRepository(): ShortenRepository {
        return FakeShortenRepository()
    }
}
