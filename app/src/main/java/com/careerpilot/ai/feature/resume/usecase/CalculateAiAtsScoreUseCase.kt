package com.careerpilot.ai.feature.resume.usecase

import com.careerpilot.ai.feature.resume.model.AiResumeAnalysis
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateAiAtsScoreUseCase @Inject constructor() {

    operator fun invoke(
        analysis: AiResumeAnalysis
    ): Int {

        val skillsScore = calculateMatchPercentage(
            matched = analysis.matchedSkills.size,
            missing = analysis.missingSkills.size
        )

        val keywordScore = calculateMatchPercentage(
            matched = analysis.matchedKeywords.size,
            missing = analysis.missingKeywords.size
        )

        val experienceScore = calculateMatchPercentage(
            matched = analysis.matchedExperienceRequirements.size,
            missing = analysis.missingExperienceRequirements.size
        )

        val educationScore = calculateMatchPercentage(
            matched = analysis.matchedEducationRequirements.size,
            missing = analysis.missingEducationRequirements.size
        )

        /*
         * Overall AI ATS Score
         *
         * Skills & Technologies = 40%
         * ATS Keywords          = 25%
         * Experience            = 15%
         * Education             = 10%
         * Base Career Fit       = 10%
         */

        val score =
            (skillsScore * 0.40) +
                    (keywordScore * 0.25) +
                    (experienceScore * 0.15) +
                    (educationScore * 0.10) +
                    10.0

        return score
            .roundToInt()
            .coerceIn(0, 100)
    }

    private fun calculateMatchPercentage(
        matched: Int,
        missing: Int
    ): Double {

        val total = matched + missing

        /*
         * If Gemini did not identify any requirements
         * in a category, don't penalize the candidate.
         */
        if (total == 0) {
            return 100.0
        }

        return (
                matched.toDouble() /
                        total.toDouble() *
                        100
                )
    }
}