package com.careerpilot.ai

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.careerpilot.ai.navigation.CareerPilotNavGraph
import com.careerpilot.ai.ui.theme.CareerPilotAITheme

@Composable
fun CareerPilotApp() {

    CareerPilotAITheme {

        Surface {
            CareerPilotNavGraph()
        }

    }

}