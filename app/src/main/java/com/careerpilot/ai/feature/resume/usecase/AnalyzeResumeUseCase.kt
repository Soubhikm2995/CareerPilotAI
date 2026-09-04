package com.careerpilot.ai.feature.resume.usecase

import com.careerpilot.ai.feature.resume.model.AtsAnalysisResult

class AnalyzeResumeUseCase(
    private val extractKeywordsUseCase: ExtractKeywordsUseCase,
    private val calculateScoreUseCase: CalculateScoreUseCase,
    private val skillDetector: SkillDetector
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
            .filterTo(LinkedHashSet()) { it in resumeKeywords }

        val missingKeywords = jobKeywords
            .filterTo(LinkedHashSet()) { it !in resumeKeywords }

        val keywordMatchPercentage =
            if (jobKeywords.isEmpty()) {
                0
            } else {
                (
                        matchedKeywords.size.toDouble() /
                                jobKeywords.size.toDouble() * 100
                        ).toInt()
                    .coerceIn(0, 100)
            }

        val resumeSkills =
            skillDetector(resumeText)

        val jobSkills =
            skillDetector(jobDescription)

        val matchedSkills = jobSkills
            .filterTo(LinkedHashSet()) { it in resumeSkills }

        val missingSkills = jobSkills
            .filterTo(LinkedHashSet()) { it !in resumeSkills }

        val score = calculateScoreUseCase(
            matchedKeywords = matchedKeywords.toList(),
            totalKeywords = jobKeywords.size,
            matchedSkills = matchedSkills.toList(),
            totalSkills = jobSkills.size
        )

        return AtsAnalysisResult(
            score = score,
            matchedKeywords = matchedKeywords.toList(),
            missingKeywords = missingKeywords.toList(),
            keywordMatchPercentage = keywordMatchPercentage,
            matchedSkills = matchedSkills.toList(),
            missingSkills = missingSkills.toList()
        )
    }
}