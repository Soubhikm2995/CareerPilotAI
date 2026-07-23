package com.careerpilot.ai.feature.dashboard.repository

import com.careerpilot.ai.feature.dashboard.model.DashboardData
import com.careerpilot.ai.feature.dashboard.model.DashboardFeature

class DashboardRepository {

    fun getDashboardFeatures(): List<DashboardFeature> {
        return DashboardData.features
    }
}