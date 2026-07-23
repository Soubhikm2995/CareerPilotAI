package com.careerpilot.ai.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.careerpilot.ai.feature.dashboard.model.DashboardFeature
import com.careerpilot.ai.feature.dashboard.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val repository = DashboardRepository()

    private val _features =
        MutableStateFlow<List<DashboardFeature>>(emptyList())

    val features: StateFlow<List<DashboardFeature>> =
        _features.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        _features.value = repository.getDashboardFeatures()
    }
}