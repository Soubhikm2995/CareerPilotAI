package com.careerpilot.ai.feature.resume.usecase

import com.careerpilot.ai.core.ai.GeminiService
import com.careerpilot.ai.feature.resume.data.AiResumeAnalysisParser
import com.careerpilot.ai.feature.resume.model.AiResumeAnalysis
import javax.inject.Inject

class AiResumeAnalysisUseCase @Inject constructor(
    private val geminiService: GeminiService,
    private val parser: AiResumeAnalysisParser
) {

    suspend operator fun invoke(
        resumeText: String,
        jobDescription: String
    ): AiResumeAnalysis {

        val prompt = """
            You are an expert ATS resume analyzer.

            Analyze the candidate's resume against the provided job description.

            Return ONLY valid JSON.
            Do not include markdown.
            Do not include ```json.
            Do not include explanations outside the JSON.

            The JSON must contain exactly these fields:

            {
              "skills": [],
              "keywords": [],
              "experienceRequirements": [],
              "educationRequirements": [],
              "matchedSkills": [],
              "missingSkills": [],
              "matchedKeywords": [],
              "missingKeywords": []
            }

            Rules:
            - skills: important skills found in the resume.
            - keywords: important job-related keywords from the job description.
            - experienceRequirements: experience requirements stated or clearly implied by the job description.
            - educationRequirements: education requirements stated or clearly implied by the job description.
            - matchedSkills: job-description skills that are supported by the resume.
            - missingSkills: job-description skills that are not supported by the resume.
            - matchedKeywords: important job-description keywords supported by the resume.
            - missingKeywords: important job-description keywords not supported by the resume.
            - Use concise strings.
            - Do not invent candidate experience, skills, education, or qualifications.

            RESUME:
            $resumeText

            JOB DESCRIPTION:
            $jobDescription
        """.trimIndent()

        val response = geminiService.generateText(prompt)

        return parser.parse(response)
    }
}