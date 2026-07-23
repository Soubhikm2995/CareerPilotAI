package com.careerpilot.ai.feature.dashboard.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.careerpilot.ai.feature.dashboard.model.DashboardFeature

@Composable
fun FeatureCard(
    feature: DashboardFeature,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = feature.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}