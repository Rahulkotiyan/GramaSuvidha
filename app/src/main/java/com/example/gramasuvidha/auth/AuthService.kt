package com.example.gramasuvidha.auth

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String? = null,
    val village: String? = null
)

class AuthService(context: Context) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val _authState = MutableStateFlow(
        AuthState(
            isAuthenticated = sharedPreferences.getBoolean("is_authenticated", false),
            user = loadUserFromPrefs()
        )
    )
    
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private fun loadUserFromPrefs(): User? {
        val userId = sharedPreferences.getString("user_id", null)
        val email = sharedPreferences.getString("user_email", null)
        val fullName = sharedPreferences.getString("user_full_name", null)
        val phone = sharedPreferences.getString("user_phone", null)
        val village = sharedPreferences.getString("user_village", null)
        
        return if (userId != null && email != null && fullName != null) {
            User(userId, email, fullName, phone, village)
        } else null
    }
    
    suspend fun login(email: String, password: String): Result<User> {
        _authState.value = _authState.value.copy(isLoading = true, error = null)
        
        return try {
            // Mock authentication - replace with actual API call
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    id = "1",
                    email = email,
                    fullName = email.split("@").first(),
                    phone = null,
                    village = null
                )
                
                saveUserToPrefs(user)
                _authState.value = _authState.value.copy(
                    isAuthenticated = true,
                    user = user,
                    isLoading = false
                )
                Result.success(user)
            } else {
                throw Exception("Invalid credentials")
            }
        } catch (e: Exception) {
            _authState.value = _authState.value.copy(
                isLoading = false,
                error = e.message
            )
            Result.failure(e)
        }
    }
    
    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        phone: String? = null,
        village: String? = null
    ): Result<User> {
        _authState.value = _authState.value.copy(isLoading = true, error = null)
        
        return try {
            // Mock registration - replace with actual API call
            if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                val user = User(
                    id = System.currentTimeMillis().toString(),
                    email = email,
                    fullName = fullName,
                    phone = phone,
                    village = village
                )
                
                saveUserToPrefs(user)
                _authState.value = _authState.value.copy(
                    isAuthenticated = true,
                    user = user,
                    isLoading = false
                )
                Result.success(user)
            } else {
                throw Exception("Invalid registration data")
            }
        } catch (e: Exception) {
            _authState.value = _authState.value.copy(
                isLoading = false,
                error = e.message
            )
            Result.failure(e)
        }
    }
    
    fun logout() {
        sharedPreferences.edit().clear().apply()
        _authState.value = AuthState()
    }
    
    private fun saveUserToPrefs(user: User) {
        sharedPreferences.edit().apply {
            putBoolean("is_authenticated", true)
            putString("user_id", user.id)
            putString("user_email", user.email)
            putString("user_full_name", user.fullName)
            putString("user_phone", user.phone)
            putString("user_village", user.village)
        }.apply()
    }
}
