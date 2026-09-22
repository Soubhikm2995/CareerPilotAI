package com.careerpilot.ai.feature.resume.usecase

import com.careerpilot.ai.feature.resume.model.AiResumeAnalysis
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateAiAtsScoreUseCase @Inject constructor() {

    operator fun invoke(analysis: AiResumeAnalysis): Int {

        val totalSkills =
            analysis.matchedSkills.size + analysis.missingSkills.size

        val totalKeywords =
            analysis.matchedKeywords.size + analysis.missingKeywords.size

        val skillsMatch = if (totalSkills > 0) {
            analysis.matchedSkills.size.toDouble() / totalSkills * 100
        } else {
            0.0
        }

        val keywordMatch = if (totalKeywords > 0) {
            analysis.matchedKeywords.size.toDouble() / totalKeywords * 100
        } else {
            0.0
        }

        return (
                skillsMatch * 0.60 +
                        keywordMatch * 0.40
                ).roundToInt().coerceIn(0, 100)
    }
}