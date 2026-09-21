package com.careerpilot.ai.core.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import javax.inject.Inject

class GeminiService @Inject constructor() {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-3.7-flash")

    suspend fun generateText(prompt: String): String {
        val response = model.generateContent(prompt)

        return response.text
            ?: throw IllegalStateException("Gemini returned an empty response.")
    }
}