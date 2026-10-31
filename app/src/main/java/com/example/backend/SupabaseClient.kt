package com.example.backend

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseClient {
    private const val FALLBACK_BASE_URL = "https://placeholder-project.supabase.co/"

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val apiKey = SupabaseConfig.currentAnonKey

        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")

        if (apiKey.isNotBlank()) {
            requestBuilder
                .header("apikey", apiKey)
                .header("Authorization", "Bearer $apiKey")
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private var cachedRetrofit: Retrofit? = null
    private var cachedBaseUrl: String? = null
    private var cachedApi: SupabaseApi? = null

    @Synchronized
    fun getApi(): SupabaseApi {
        val currentBaseUrl = if (SupabaseConfig.isConfigured) {
            SupabaseConfig.getNormalizedBaseUrl()
        } else {
            FALLBACK_BASE_URL
        }

        if (cachedApi == null || cachedBaseUrl != currentBaseUrl) {
            val retrofit = Retrofit.Builder()
                .baseUrl(currentBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            cachedRetrofit = retrofit
            cachedBaseUrl = currentBaseUrl
            cachedApi = retrofit.create(SupabaseApi::class.java)
        }

        return cachedApi!!
    }

    @Synchronized
    fun invalidate() {
        cachedRetrofit = null
        cachedBaseUrl = null
        cachedApi = null
    }
}
