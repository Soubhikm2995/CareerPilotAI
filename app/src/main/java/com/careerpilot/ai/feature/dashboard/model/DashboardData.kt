package com.careerpilot.ai.feature.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Work
import com.careerpilot.ai.navigation.Routes

object DashboardData {

    val features = listOf(
        DashboardFeature(
            title = "Resume Optimizer",
            description = "Optimize your resume for ATS",
            icon = Icons.Default.Description,
            route = Routes.RESUME
        ),
        DashboardFeature(
            title = "AI Interview Coach",
            description = "Practice mock interviews",
            icon = Icons.Default.RecordVoiceOver,
            route = Routes.INTERVIEW
        ),
        DashboardFeature(
            title = "Job Tracker",
            description = "Track your job applications",
            icon = Icons.Default.Work,
            route = Routes.JOBS
        ),
        DashboardFeature(
            title = "ATS Scanner",
            description = "Analyze ATS compatibility",
            icon = Icons.AutoMirrored.Filled.Article,
            route = Routes.RESUME
        ),
        DashboardFeature(
            title = "Cover Letter",
            description = "Generate professional cover letters",
            icon = Icons.Default.Badge,
            route = Routes.COVER_LETTER
        ),
        DashboardFeature(
            title = "Profile",
            description = "Manage your account",
            icon = Icons.Default.Person,
            route = Routes.PROFILE
        )
    )
}