package com.careerpilot.ai.feature.resume.usecase

import com.careerpilot.ai.feature.resume.model.AtsAnalysisResult

class AnalyzeResumeUseCase(
    private val extractKeywordsUseCase: ExtractKeywordsUseCase,
    private val calculateScoreUseCase: CalculateScoreUseCase
) {

    operator fun invoke(
        resumeText: String,
        jobDescription: String
    ): AtsAnalysisResult {

        val resumeKeywords =
            extractKeywordsUseCase(resumeText)

        val jobKeywords =
            extractKeywordsUseCase(jobDescription)

        val matchedKeywords = jobKeywords
            .filter { it in resumeKeywords }
            .distinct()

        val missingKeywords = jobKeywords
            .filter { it !in resumeKeywords }
            .distinct()

        val score = calculateScoreUseCase(
            matchedKeywords = matchedKeywords,
            totalKeywords = jobKeywords.size
        )

        return AtsAnalysisResult(
            score = score,
            matchedKeywords = matchedKeywords,
            missingKeywords = missingKeywords
        )
    }
}