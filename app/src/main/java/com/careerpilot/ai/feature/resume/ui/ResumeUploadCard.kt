package com.careerpilot.ai.feature.resume.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResumeUploadCard(
    fileName: String?,
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Your Resume",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = fileName ?: "No resume uploaded yet",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onUploadClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (fileName == null) {
                        "Upload Resume"
                    } else {
                        "Change Resume"
                    }
                )
            }
        }
    }
}