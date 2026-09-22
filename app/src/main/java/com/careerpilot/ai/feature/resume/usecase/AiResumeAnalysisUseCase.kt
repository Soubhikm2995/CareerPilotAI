package com.careerpilot.ai.feature.resume.usecase

import com.careerpilot.ai.core.ai.AiProvider
import com.careerpilot.ai.feature.resume.data.AiResumeAnalysisParser
import com.careerpilot.ai.feature.resume.model.AiResumeAnalysis
import javax.inject.Inject

class AiResumeAnalysisUseCase @Inject constructor(
    private val aiProvider: AiProvider,
    private val parser: AiResumeAnalysisParser
) {

    suspend operator fun invoke(
        resumeText: String,
        jobDescription: String
    ): AiResumeAnalysis {

            val prompt = """
        You are an expert ATS resume analyzer and professional recruiter.
    
        Analyze the candidate's resume against the provided job description.
    
        Your goal is to identify the most relevant requirements and determine
        how closely the resume matches the specific job.
    
        Return ONLY valid JSON.
        Do not include markdown.
        Do not include ```json.
        Do not include explanations outside the JSON.
    
        The JSON must contain exactly these fields:
    
        {
          "skills": [],
          "keywords": [],
    
          "experienceRequirements": [],
          "matchedExperienceRequirements": [],
          "missingExperienceRequirements": [],
    
          "educationRequirements": [],
          "matchedEducationRequirements": [],
          "missingEducationRequirements": [],
    
          "matchedSkills": [],
          "missingSkills": [],
    
          "matchedKeywords": [],
          "missingKeywords": []
        }
    
        IMPORTANT ANALYSIS RULES:
    
        1. SKILLS
        - Include specific technical skills, tools, technologies, methodologies,
          platforms, domain skills, and clearly stated professional skills.
        - Examples: Python, SQL, Power BI, Excel, Tableau, AWS, Salesforce,
          data analysis, machine learning, stakeholder management.
        - Do not include generic words such as "work", "team", "business",
          "professional", "experience", "responsibilities", or "ability".
    
        2. KEYWORDS
        - Extract only important ATS-relevant terms from the job description.
        - Prioritize job titles, technical terms, tools, technologies,
          methodologies, domain terminology, certifications, and important
          business capabilities.
        - Prefer meaningful phrases over individual generic words.
        - Do NOT include common English words.
        - Do NOT include generic terms such as:
          "role", "candidate", "company", "team", "work", "responsibilities",
          "skills", "experience", "knowledge", "ability", "strong", "good",
          "excellent", "required", "preferred", "including", "support".
        - Do not split an important phrase unnecessarily.
          For example, prefer "data analysis" rather than separately returning
          "data" and "analysis".
    
        3. MATCHED SKILLS
        - Include only job-description skills that are clearly supported by
          the resume.
        - Do not infer skills that are not explicitly supported.
        - Similar terminology may be considered a match when the meaning is
          clearly equivalent.
        - Do not invent candidate experience.
    
        4. MISSING SKILLS
        - Include important job-description skills that are not supported by
          the resume.
        - Do not list generic requirements as missing skills.
        - Do not mark a skill as missing if the resume clearly demonstrates
          an equivalent capability.
    
        5. MATCHED KEYWORDS
        - Include only important job-description keywords that are supported
          by the resume.
        - Use meaningful phrases whenever possible.
    
        6. MISSING KEYWORDS
        - Include only important ATS-relevant keywords that appear in the job
          description but are not supported by the resume.
        - Do not fill this list with generic English words.
        - Do not duplicate items that are already in missingSkills.
    
        7. EXPERIENCE REQUIREMENTS
        - Extract specific experience requirements from the job description.
        - Examples:
          "3+ years of experience in data analysis",
          "experience with Power BI",
          "experience in aviation operations".
        - Do not invent requirements.
    
        8. MATCHED EXPERIENCE REQUIREMENTS
        - Include experience requirements that are supported by the resume.
        - Only mark a requirement as matched when the resume provides clear
          evidence.
        - Consider years of experience, relevant job functions, tools, and
          domain experience when evidence is explicit.
        - Do not assume experience that is not stated.
    
        9. MISSING EXPERIENCE REQUIREMENTS
        - Include specific experience requirements that are not supported by
          the resume.
        - Do not list generic requirements.
        - Do not mark an experience requirement as missing when the resume
          clearly demonstrates an equivalent requirement.
    
        10. EDUCATION REQUIREMENTS
        - Extract explicit or clearly implied education requirements.
        - Examples:
          "Bachelor's degree in Computer Science",
          "Master's degree in Business Analytics".
        - Do not invent requirements.
    
        11. MATCHED EDUCATION REQUIREMENTS
        - Include education requirements that are clearly supported by the
          candidate's resume.
        - Consider degree level and field when explicitly available.
        - Do not assume an equivalent degree without evidence.
    
        12. MISSING EDUCATION REQUIREMENTS
        - Include education requirements that are not supported by the resume.
        - Do not invent missing qualifications.
    
        13. ACCURACY
        - Base all conclusions only on the supplied resume and job description.
        - Never invent qualifications, experience, skills, certifications,
          education, or achievements.
        - If evidence is unclear, leave the item out rather than guessing.
        - Keep all lists concise and relevant.
    
        RESUME:
        $resumeText
    
        JOB DESCRIPTION:
        $jobDescription
    """.trimIndent()

        val response = aiProvider.generateText(prompt)

        return parser.parse(response)
    }
}