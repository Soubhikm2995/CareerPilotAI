package com.careerpilot.ai.feature.dashboard.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardFeature(

    val title: String,

    val description: String,

    val icon: ImageVector,

    val route: String

)