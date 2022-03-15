package com.stash.shopeklobek.model.api

import com.stash.shopeklobek.BuildConfig
import com.stash.shopeklobek.model.interfaces.CurrencyConverterService
import com.stash.shopeklobek.model.interfaces.PlacesServices
import com.stash.shopeklobek.utils.Constants
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PlacesApi {
    private lateinit var retrofit: Retrofit
    private val retro: Retrofit by lazy {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_PLACES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildAuthClient())
            .build()
        retrofit
    }

    var findPlaceApi: PlacesServices = retro.create(PlacesServices::class.java)

    private fun authInterceptor(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header("X-RapidAPI-Host", Constants.X_RapidAPI_Host)
                .header("X-RapidAPI-Key",Constants.X_RapidAPI_Key)
                .build()
        )
    }

    private fun buildAuthClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(provideLoggingInterceptor())
            .addInterceptor(::authInterceptor)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        return httpClient
            .build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()

        @Suppress("ConstantConditionIf")
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }
}