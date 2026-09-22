package com.careerpilot.ai.core.ai

interface AiProvider {

    suspend fun generateText(
        prompt: String
    ): String
}