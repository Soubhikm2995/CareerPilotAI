package com.careerpilot.ai.feature.resume.usecase

class SkillDetector {

    private val skills = setOf(
        "python",
        "java",
        "kotlin",
        "javascript",
        "typescript",
        "c++",
        "c#",
        "sql",
        "excel",
        "power bi",
        "tableau",
        "aws",
        "azure",
        "gcp",
        "docker",
        "kubernetes",
        "git",
        "github",
        "machine learning",
        "deep learning",
        "artificial intelligence",
        "generative ai",
        "gen ai",
        "llm",
        "natural language processing",
        "nlp",
        "data analysis",
        "data science",
        "business analysis",
        "business intelligence",
        "product management",
        "project management",
        "program management",
        "agile",
        "scrum",
        "jira",
        "stakeholder management",
        "leadership",
        "communication",
        "problem solving",
        "critical thinking",
        "sales",
        "business development",
        "customer service",
        "customer experience",
        "operations",
        "supply chain",
        "marketing",
        "seo",
        "crm",
        "salesforce"
    )

    private val WHITESPACE = Regex("\\s+")

    operator fun invoke(text: String): Set<String> {

        val normalizedText = text
            .lowercase()
            .replace(WHITESPACE, " ")
            .trim()

        val tokens = normalizedText.split(" ")
        val tokenSet = tokens.toHashSet()

        val bigramSet = HashSet<String>(tokens.size)
        val trigramSet = HashSet<String>(tokens.size)
        for (i in 0 until tokens.size) {
            if (i + 1 < tokens.size) {
                bigramSet.add(tokens[i] + " " + tokens[i + 1])
            }
            if (i + 2 < tokens.size) {
                trigramSet.add(tokens[i] + " " + tokens[i + 1] + " " + tokens[i + 2])
            }
        }

        val found = HashSet<String>()

        for (skill in skills) {
            val tokenCount = skill.count{it==' '} + 1

            val matched = when (tokenCount) {
                1 -> tokenSet.contains(skill)
                2 -> bigramSet.contains(skill)
                else -> trigramSet.contains(skill)
            }

            if (matched) {
                found.add(skill)
            }
        }

        return found

    }
}