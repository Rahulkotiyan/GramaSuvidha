package com.example.gramasuvidha.network

import retrofit2.Response

/**
 * Phase 3: Network Result Wrapper
 * Sealed class to handle different states of network operations
 */
sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val code: Int? = null) : NetworkResult<T>()
    data class Loading<T>(val isLoading: Boolean = true) : NetworkResult<T>()
    
    companion object {
        /**
         * Create a success result
         */
        fun <T> success(data: T): NetworkResult<T> = Success(data)
        
        /**
         * Create an error result
         */
        fun <T> error(message: String, code: Int? = null): NetworkResult<T> = Error(message, code)
        
        /**
         * Create a loading result
         */
        fun <T> loading(): NetworkResult<T> = Loading()
    }
}

/**
 * Extension function to convert Retrofit Response to NetworkResult
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return NetworkResult.success(body)
            }
        }
        return NetworkResult.error(
            message = response.message() ?: "Unknown error occurred",
            code = response.code()
        )
    } catch (e: Exception) {
        return NetworkResult.error(
            message = e.message ?: "Network error occurred",
            code = null
        )
    }
}
