package com.careerpilot.ai.feature.resume.data

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

class GeminiAiService {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-3.7-flash")

    suspend fun testConnection(): String {
        val response = model.generateContent(
            "Reply with exactly: CareerPilot AI Gemini connection successful"
        )

        return response.text ?: "No response from Gemini"
    }
}