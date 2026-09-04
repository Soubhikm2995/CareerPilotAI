package com.careerpilot.ai.feature.resume.usecase

class ExtractKeywordsUseCase {

    operator fun invoke(text: String): Set<String> {

        val normalizedText = text
            .lowercase()
            .replace("&", " and ")
            .replace("/", " ")
            .replace("-", " ")
            .replace(Regex("[^a-z0-9+#.]"), " ")

        val words = normalizedText
            .split(Regex("\\s+"))
            .map { it.trim() }
            .filter { it.length >= 3 }
            .filterNot { it in stopWords }
            .filterNot { it in genericJobWords }

        return words.toSet()
    }

    private val stopWords = setOf(
        "the",
        "and",
        "for",
        "with",
        "from",
        "this",
        "that",
        "are",
        "you",
        "your",
        "our",
        "their",
        "will",
        "have",
        "has",
        "been",
        "being",
        "into",
        "about",
        "than",
        "then",
        "they",
        "them",
        "these",
        "those",
        "who",
        "what",
        "when",
        "where",
        "which",
        "while",
        "using",
        "work",
        "working",
        "role",
        "roles",
        "job",
        "candidate",
        "looking",
        "responsibilities",
        "required",
        "requirements",
        "should",
        "would",
        "could",
        "must",
        "need",
        "needs",
        "able",
        "ability",
        "also",
        "more",
        "most",
        "some",
        "such",
        "other",
        "their",
        "through",
        "within",
        "across",
        "between",
        "including",
        "etc"
    )

    private val genericJobWords = setOf(
        "business",
        "opportunities",
        "opportunity",
        "complex",
        "human",
        "functional",
        "expertise",
        "management",
        "design",
        "model",
        "models",
        "across",
        "results",
        "services",
        "manage",
        "team",
        "teams",
        "insights",
        "analysis",
        "supported",
        "accurate",
        "accuracy",
        "decision",
        "decisions",
        "senior",
        "regional",
        "projects",
        "project",
        "related",
        "support",
        "provide",
        "providing",
        "develop",
        "developing",
        "ensure",
        "ensuring",
        "strong",
        "excellent",
        "good",
        "great",
        "various",
        "multiple",
        "different",
        "professional",
        "experience",
        "years",
        "environment",
        "organization",
        "organization's",
        "company",
        "customers",
        "customer",
        "clients",
        "client"
    )
}