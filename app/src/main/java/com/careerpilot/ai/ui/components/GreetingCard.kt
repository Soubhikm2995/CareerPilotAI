package com.careerpilot.ai.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GreetingCard(
    userName: String = "Soubhik"
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "👋 Welcome Back,",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Let's build your dream career today 🚀",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}