package com.careerpilot.ai.core.ai

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRouter @Inject constructor(
    private val geminiService: GeminiService,
    private val openRouterService: OpenRouterService
) : AiProvider {

    override suspend fun generateText(
        prompt: String
    ): String {

        return try {

            // Primary provider
            geminiService.generateText(prompt)

        } catch (geminiException: Exception) {

            // Fallback provider
            try {

                openRouterService.generateText(prompt)

            } catch (openRouterException: Exception) {

                throw IllegalStateException(
                    "All AI providers failed. " +
                            "Gemini: ${geminiException.message}. " +
                            "OpenRouter: ${openRouterException.message}.",
                    openRouterException
                )
            }
        }
    }
}