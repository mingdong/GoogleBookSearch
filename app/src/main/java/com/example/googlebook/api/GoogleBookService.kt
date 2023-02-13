package com.example.googlebook.api

import com.example.googlebook.BuildConfig
import com.example.googlebook.api.GoogleBookApi.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber


fun createApiService(): GoogleBookApi {
  val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
      val request = chain.request().run {
        val url = url()
          .newBuilder()
          .build()

        newBuilder()
          .addHeader("Content-Type", "application/json;charset=utf-8")
          .url(url)
          .build()
      }
      chain.proceed(request)
    }
    .addInterceptor(loggingInterceptor())
    .build()

  val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .build()

  return retrofit.create(GoogleBookApi::class.java)
}

fun loggingInterceptor() =
  HttpLoggingInterceptor { message -> Timber.tag("Http").v(message) }.apply {
    level = if (BuildConfig.DEBUG) BODY else NONE
  }
