package com.careerpilot.ai.resume.data

import com.careerpilot.ai.feature.resume.data.AiResumeAnalysisParser
import org.junit.Assert
import org.junit.Test

class AiResumeAnalysisParserTest {

    @Test
    fun parseValidAiResponse() {

        val json = """
            {
              "skills": [
                "Python",
                "SQL",
                "Power BI"
              ],
              "keywords": [
                "Data Analysis",
                "Business Intelligence"
              ],
              "experienceRequirements": [
                "3+ years of experience",
                "Stakeholder management"
              ],
              "educationRequirements": [
                "Bachelor's degree"
              ],
              "matchedSkills": [
                "Python",
                "SQL"
              ],
              "missingSkills": [
                "Power BI"
              ],
              "matchedKeywords": [
                "Data Analysis"
              ],
              "missingKeywords": [
                "Business Intelligence"
              ]
            }
        """.trimIndent()

        val parser = AiResumeAnalysisParser()

        val result = parser.parse(json)

        Assert.assertEquals(
            listOf("Python", "SQL", "Power BI"),
            result.skills
        )

        Assert.assertEquals(
            listOf("Data Analysis", "Business Intelligence"),
            result.keywords
        )

        Assert.assertEquals(
            listOf("Python", "SQL"),
            result.matchedSkills
        )

        Assert.assertEquals(
            listOf("Power BI"),
            result.missingSkills
        )

        Assert.assertEquals(
            listOf("Data Analysis"),
            result.matchedKeywords
        )

        Assert.assertEquals(
            listOf("Business Intelligence"),
            result.missingKeywords
        )
    }
}