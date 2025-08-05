package ru.netology.nmedia.api

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Response
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth

fun loggingInterceptor() = HttpLoggingInterceptor()
    .apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

fun authInterceptor(auth: AppAuth): Interceptor = Interceptor { chain ->
    val token = auth.authStateFlow.value.token
    val requestBuilder = chain.request().newBuilder()
    if (!token.isNullOrBlank()) {
        requestBuilder.addHeader("Authorization", "Bearer $token")
    }
    chain.proceed(requestBuilder.build())
}


