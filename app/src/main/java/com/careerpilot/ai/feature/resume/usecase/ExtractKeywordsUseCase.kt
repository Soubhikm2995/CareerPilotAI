package com.careerpilot.ai.feature.resume.usecase

class ExtractKeywordsUseCase {

    operator fun invoke(text: String): Set<String> {

        return text
            .lowercase()
            .replace(Regex("[^a-z0-9+#.]"), " ")
            .split(Regex("\\s+"))
            .filter { word ->
                word.length >= 3
            }
            .toSet()
    }
}