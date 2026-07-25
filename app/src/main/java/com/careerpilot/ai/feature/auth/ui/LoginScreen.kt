package com.careerpilot.ai.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.careerpilot.ai.feature.auth.viewmodel.AuthViewModel
import com.careerpilot.ai.navigation.Routes
import com.careerpilot.ai.ui.components.CPButton
import com.careerpilot.ai.ui.components.CPGradientBackground
import com.careerpilot.ai.ui.components.CPLogo
import com.careerpilot.ai.ui.components.CPTextField

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    // Navigate to Dashboard after successful login
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Routes.DASHBOARD) {
                popUpTo(Routes.LOGIN) {
                    inclusive = true
                }
            }
        }
    }

    CPGradientBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            CPLogo()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome Back 👋",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            CPTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CPTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true
            )

            uiState.error?.let { error ->

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            CPButton(
                text = "Sign In",
                isLoading = uiState.isLoading,
                onClick = {
                    viewModel.login(
                        email = email.trim(),
                        password = password
                    )
                }
            )
        }
    }
}