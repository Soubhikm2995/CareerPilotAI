package com.careerpilot.ai.core.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class GeminiService @Inject constructor() : AiProvider {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-3.8-flash")

    override suspend fun generateText(
        prompt: String
    ): String {

        var lastException: Exception? = null

        repeat(3) { attempt ->

            try {

                val response = model.generateContent(prompt)

                return response.text
                    ?: throw IllegalStateException(
                        "Gemini returned an empty response."
                    )

            } catch (e: Exception) {

                lastException = e

                if (attempt < 2) {

                    delay(
                        when (attempt) {
                            0 -> 2.seconds
                            else -> 4.seconds
                        }
                    )
                }
            }
        }

        throw lastException
            ?: IllegalStateException(
                "Unable to generate a Gemini response."
            )
    }
}