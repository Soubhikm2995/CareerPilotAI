package com.careerpilot.ai.feature.resume.usecase

class CalculateScoreUseCase {

    operator fun invoke(
        matchedKeywords: List<String>,
        totalKeywords: Int
    ): Int {

        if (totalKeywords == 0) {
            return 0
        }

        return (
                matchedKeywords.size.toDouble() /
                        totalKeywords.toDouble() * 100
                ).toInt().coerceIn(0, 100)
    }
}