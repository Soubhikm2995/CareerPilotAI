package com.careerpilot.ai.feature.resume.model

data class AiResumeAnalysis(
    val skills: List<String>,
    val keywords: List<String>,

    val experienceRequirements: List<String>,
    val matchedExperienceRequirements: List<String>,
    val missingExperienceRequirements: List<String>,

    val educationRequirements: List<String>,
    val matchedEducationRequirements: List<String>,
    val missingEducationRequirements: List<String>,

    val matchedSkills: List<String>,
    val missingSkills: List<String>,

    val matchedKeywords: List<String>,
    val missingKeywords: List<String>
)