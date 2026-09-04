package com.careerpilot.ai.feature.resume.usecase

class CalculateScoreUseCase {

    operator fun invoke(
        matchedKeywords: List<String>,
        totalKeywords: Int,
        matchedSkills: List<String> = emptyList(),
        totalSkills: Int = 0
    ): Int {

        val keywordPercentage =
            if (totalKeywords == 0) {
                0.0
            } else {
                matchedKeywords.size.toDouble() /
                        totalKeywords.toDouble() * 100
            }

        val skillPercentage =
            if (totalSkills == 0) {
                0.0
            } else {
                matchedSkills.size.toDouble() /
                        totalSkills.toDouble() * 100
            }

        val overallScore = if (totalSkills == 0) {
            keywordPercentage
        } else {
            (keywordPercentage * 0.60) +
                    (skillPercentage * 0.40)
        }

        return overallScore
            .toInt()
            .coerceIn(0, 100)
    }
}