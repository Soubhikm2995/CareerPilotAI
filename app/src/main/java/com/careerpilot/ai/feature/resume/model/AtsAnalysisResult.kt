package com.careerpilot.ai.feature.resume.model

data class AtsAnalysisResult(
    val score: Int,
    val matchedKeywords: List<String>,
    val missingKeywords: List<String>
)