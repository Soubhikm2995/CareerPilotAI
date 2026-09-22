package com.careerpilot.ai.feature.resume.data

import com.careerpilot.ai.feature.resume.model.AiResumeAnalysis
import org.json.JSONObject
import javax.inject.Inject

class AiResumeAnalysisParser @Inject constructor() {

    fun parse(jsonText: String): AiResumeAnalysis {
        val json = JSONObject(jsonText)

        return AiResumeAnalysis(
            skills = json.getStringList("skills"),
            keywords = json.getStringList("keywords"),

            experienceRequirements =
                json.getStringList("experienceRequirements"),

            matchedExperienceRequirements =
                json.getStringList("matchedExperienceRequirements"),

            missingExperienceRequirements =
                json.getStringList("missingExperienceRequirements"),

            educationRequirements =
                json.getStringList("educationRequirements"),

            matchedEducationRequirements =
                json.getStringList("matchedEducationRequirements"),

            missingEducationRequirements =
                json.getStringList("missingEducationRequirements"),

            matchedSkills =
                json.getStringList("matchedSkills"),

            missingSkills =
                json.getStringList("missingSkills"),

            matchedKeywords =
                json.getStringList("matchedKeywords"),

            missingKeywords =
                json.getStringList("missingKeywords")
        )
    }

    private fun JSONObject.getStringList(
        key: String
    ): List<String> {

        val array = optJSONArray(key) ?: return emptyList()

        return buildList {

            for (index in 0 until array.length()) {

                val value =
                    array.optString(index)
                        .trim()

                if (value.isNotEmpty()) {
                    add(value)
                }
            }
        }
    }
}