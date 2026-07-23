package com.careerpilot.ai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.careerpilot.ai.feature.dashboard.ui.HomeScreen
import com.careerpilot.ai.feature.resume.ui.CoverLetterScreen
import com.careerpilot.ai.feature.resume.ui.InterviewScreen
import com.careerpilot.ai.feature.resume.ui.JobTrackerScreen
import com.careerpilot.ai.feature.resume.ui.ProfileScreen
import com.careerpilot.ai.feature.resume.ui.ResumeScreen
import com.careerpilot.ai.feature.splash.ui.SplashScreen

@Composable
fun CareerPilotNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }

        composable(Routes.DASHBOARD) {
            HomeScreen(navController)
        }

        composable(Routes.RESUME) {
            ResumeScreen()
        }

        composable(Routes.COVER_LETTER) {
            CoverLetterScreen()
        }

        composable(Routes.INTERVIEW) {
            InterviewScreen()
        }

        composable(Routes.JOBS) {
            JobTrackerScreen()
        }

        composable(Routes.PROFILE) {
            ProfileScreen()
        }
    }
}