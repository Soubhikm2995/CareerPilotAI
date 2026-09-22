package com.careerpilot.ai.feature.resume.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.careerpilot.ai.feature.resume.model.AiResumeAnalysis
import com.careerpilot.ai.feature.resume.usecase.AiResumeAnalysisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResumeAiUiState(
    val isLoading: Boolean = false,
    val result: AiResumeAnalysis? = null,
    val error: String? = null
)

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val aiResumeAnalysisUseCase: AiResumeAnalysisUseCase
) : ViewModel() {

    private val _aiState = MutableStateFlow(ResumeAiUiState())
    val aiState: StateFlow<ResumeAiUiState> = _aiState.asStateFlow()

    fun analyzeWithAi(
        resumeText: String,
        jobDescription: String
    ) {
        if (resumeText.isBlank() || jobDescription.isBlank()) {
            _aiState.value = ResumeAiUiState(
                error = "Resume and job description are required."
            )
            return
        }

        viewModelScope.launch {
            _aiState.value = ResumeAiUiState(isLoading = true)

            try {
                val result = aiResumeAnalysisUseCase(
                    resumeText = resumeText,
                    jobDescription = jobDescription
                )

                _aiState.value = ResumeAiUiState(
                    result = result
                )

            } catch (e: Exception) {
                _aiState.value = ResumeAiUiState(
                    error = e.message ?: "Unable to analyze resume with AI."
                )
            }
        }
    }

    fun clearAiResult() {
        _aiState.value = ResumeAiUiState()
    }
}