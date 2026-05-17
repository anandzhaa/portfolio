package com.anand.portfolio.network

import com.anand.portfolio.model.ContactRequest
import com.anand.portfolio.model.ContactResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// ===== API Service Interface =====
interface PortfolioApiService {
    @POST("api/contact")
    suspend fun sendContact(@Body request: ContactRequest): Response<ContactResponse>
}

// ===== API Client Singleton =====
object ApiClient {
    // IMPORTANT: Replace this with your deployed backend URL
    // Free options: Render.com, Railway.app
    private const val BASE_URL = "https://YOUR-BACKEND.onrender.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: PortfolioApiService = retrofit.create(PortfolioApiService::class.java)
}
