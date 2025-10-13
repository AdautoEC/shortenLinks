package br.com.warehouse.shortenlinks.core.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

internal class HttpRequestInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val request = originalRequest
      .newBuilder()
      .url(originalRequest.url.newBuilder()
        .build()
      ).build()
    Log.d("Request - url:", request.url.toString())
    Log.d("Request - header:", request.headers.toString())
    Log.d("Request - body:", request.body.toString())
    return chain.proceed(request)
  }
}
