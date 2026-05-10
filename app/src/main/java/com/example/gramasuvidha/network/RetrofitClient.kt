package com.example.gramasuvidha.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    
    // Base URL for your backend API
    // Replace with your Supabase project URL
    // Example: "https://your-project-id.supabase.co/rest/v1/"
    const val BASE_URL = "https://uqgeyezfbcwxfoksglrd.supabase.co/rest/v1/"
    
    // Supabase API Key (replace with your actual anon key)
    // Get from: Supabase Dashboard → Project Settings → API
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVxZ2V5ZXpmYmN3eGZva3NnbHJkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgzODg2OTgsImV4cCI6MjA5Mzk2NDY5OH0.VfaJg4O3FF1FWJpR9VZZ2MuAVLUBMv_PmlJXCajOtfI"
    
        
    // Logging interceptor for debugging network calls
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // Network interceptor to add common headers
    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("apikey", SUPABASE_ANON_KEY)
            .addHeader("Authorization", "Bearer $SUPABASE_ANON_KEY")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }
    
    // OkHttp client with configuration
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headerInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // API service instance
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
