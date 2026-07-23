package com.careerpilot.ai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CareerOverview() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        CareerStatCard(
            title = "Applications",
            value = "12",
            modifier = Modifier.weight(1f)
        )

        CareerStatCard(
            title = "Interviews",
            value = "4",
            modifier = Modifier.weight(1f)
        )

        CareerStatCard(
            title = "Resume",
            value = "92%",
            modifier = Modifier.weight(1f)
        )
    }
}