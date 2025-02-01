package com.example.pawpal.remote

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

object RetrofitClient {
    private const val BASE_URL = "http://157.230.8.219/pawpal2/"
    private const val USERNAME = "ndobermani"
    private const val PASSWORD = "vEXB6*"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Loga requestove i response data
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(USERNAME, PASSWORD))
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: KategorijaApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Koristi custom okHttpClient iznad
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(KategorijaApiService::class.java)
    }
}

class BasicAuthInterceptor(private val username: String, private val password: String) : Interceptor {
    @SuppressLint("NewApi")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // String za autentifikaciju
        val credentials = "$username:$password"
        val base64Credentials = java.util.Base64.getEncoder().encodeToString(credentials.toByteArray())

        // Dodavanje headera za autorizaciju
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Basic $base64Credentials")
            .build()

        return chain.proceed(request)
    }
}