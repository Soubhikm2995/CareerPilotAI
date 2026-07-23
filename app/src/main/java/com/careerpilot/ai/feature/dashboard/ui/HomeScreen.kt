package com.careerpilot.ai.feature.dashboard.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.careerpilot.ai.feature.dashboard.viewmodel.DashboardViewModel
import com.careerpilot.ai.ui.components.GreetingCard
import com.careerpilot.ai.ui.components.CareerOverview
import androidx.navigation.NavController
import com.careerpilot.ai.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    val viewModel: DashboardViewModel = viewModel()

    val features by viewModel.features.collectAsState()

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text("CareerPilot AI")
                }
            )
        }

    ) { padding ->

        LazyColumn(

            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding(),
                bottom = 16.dp
            )

        ) {
            item {

                GreetingCard()

                Spacer(modifier = Modifier.height(16.dp))

                CareerOverview()

                Spacer(modifier = Modifier.height(24.dp))
            }

            items(features) { feature ->

                FeatureCard(
                    feature = feature,
                    onClick = {
                        navController.navigate(feature.route)
                    }
                )
            }
        }
    }
}