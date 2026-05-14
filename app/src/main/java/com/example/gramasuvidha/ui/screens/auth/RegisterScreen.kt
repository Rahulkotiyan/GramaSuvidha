package com.example.gramasuvidha.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.auth.AuthService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
        authService: AuthService,
        onRegisterSuccess: () -> Unit,
        onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val authState by authService.authState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onRegisterSuccess()
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            // Handle error display if needed
        }
    }

    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                    modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                        text = "Register",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "Full Name")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                )

                OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                )

                OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone (Optional)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                )

                OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text("Village (Optional)") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = "Village")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                )

                OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                        if (isPasswordVisible) Icons.Default.VisibilityOff
                                        else Icons.Default.Visibility,
                                        contentDescription =
                                                if (isPasswordVisible) "Hide password"
                                                else "Show password"
                                )
                            }
                        },
                        visualTransformation =
                                if (isPasswordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                )

                OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                        },
                        trailingIcon = {
                            IconButton(
                                    onClick = {
                                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                                    }
                            ) {
                                Icon(
                                        if (isConfirmPasswordVisible) Icons.Default.VisibilityOff
                                        else Icons.Default.Visibility,
                                        contentDescription =
                                                if (isConfirmPasswordVisible) "Hide password"
                                                else "Show password"
                                )
                            }
                        },
                        visualTransformation =
                                if (isConfirmPasswordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                )

                authState.error?.let { error ->
                    Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                        onClick = {
                            if (fullName.isNotEmpty() &&
                                            email.isNotEmpty() &&
                                            password.isNotEmpty() &&
                                            password == confirmPassword
                            ) {
                                isLoading = true
                                // Launch register coroutine
                                coroutineScope.launch {
                                    authService.register(
                                            fullName = fullName,
                                            email = email,
                                            password = password,
                                            phone = phone.ifEmpty { null },
                                            village = village.ifEmpty { null }
                                    )
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled =
                                !isLoading &&
                                        fullName.isNotEmpty() &&
                                        email.isNotEmpty() &&
                                        password.isNotEmpty() &&
                                        confirmPassword.isNotEmpty() &&
                                        password == confirmPassword
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Register")
                    }
                }

                Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account? ")
                    TextButton(onClick = onNavigateToLogin, enabled = !isLoading) { Text("Login") }
                }
            }
        }
    }
}
