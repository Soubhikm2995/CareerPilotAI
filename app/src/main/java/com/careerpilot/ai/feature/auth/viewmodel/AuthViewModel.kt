package com.careerpilot.ai.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.careerpilot.ai.feature.auth.model.AuthUiState
import com.careerpilot.ai.feature.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = repository.login(email, password)

            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(
                    error = result.exceptionOrNull()?.localizedMessage
                        ?: "Login failed"
                )
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = repository.register(email, password)

            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(
                    error = result.exceptionOrNull()?.localizedMessage
                        ?: "Registration failed"
                )
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = repository.resetPassword(email)

            _uiState.value = if (result.isSuccess) {
                AuthUiState()
            } else {
                AuthUiState(
                    error = result.exceptionOrNull()?.localizedMessage
                        ?: "Password reset failed"
                )
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }
}