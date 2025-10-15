package br.com.warehouse.shortenlinks.core.network.di

import br.com.warehouse.shortenlinks.core.network.interceptor.HttpRequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

  /**
   * Provides the [OkHttpClient] instance.
   */
  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(HttpRequestInterceptor())
      .build()
  }

  /**
   * Provides the [Retrofit] instance.
   *
   * @param okHttpClient The [OkHttpClient] to be used by Retrofit.
   */
  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl("https://url-shortener-server.onrender.com/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }
}
