package com.careerpilot.ai.feature.resume.data

import com.google.firebase.Firebase
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.ai

class GeminiResumeAnalyzer {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-3.7-flash")

    suspend fun analyze(
        resumeText: String,
        jobDescription: String
    ): String {

        val prompt = """
            You are an expert ATS resume analyzer.

            Analyze the candidate's resume against the provided job description.

            RESUME:
            $resumeText

            JOB DESCRIPTION:
            $jobDescription

            Identify:
            1. Important technical and professional skills required by the job.
            2. Important ATS keywords required by the job.
            3. Experience and seniority requirements.
            4. Education and certification requirements.
            5. Which required skills are present in the resume.
            6. Which required skills are missing from the resume.
            7. Which important keywords are present.
            8. Which important keywords are missing.

            Return ONLY valid JSON using this structure:

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

            Do not include markdown.
            Do not include explanations outside the JSON.
        """.trimIndent()

        val response = model.generateContent(prompt)

        return response.text ?: "{}"
    }
}