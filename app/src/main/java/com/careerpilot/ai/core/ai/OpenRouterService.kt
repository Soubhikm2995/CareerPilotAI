package com.careerpilot.ai.core.ai

import com.careerpilot.ai.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenRouterService @Inject constructor() : AiProvider {

    private val client = OkHttpClient()

    override suspend fun generateText(
        prompt: String
    ): String = withContext(Dispatchers.IO) {

        val apiKey = BuildConfig.OPENROUTER_API_KEY

        require(apiKey.isNotBlank()) {
            "OpenRouter API key is not configured."
        }

        val requestJson = JSONObject().apply {

            put(
                "model",
                "openrouter/free"
            )

            put(
                "messages",
                JSONArray().apply {

                    put(
                        JSONObject().apply {
                            put("role", "user")
                            put("content", prompt)
                        }
                    )
                }
            )
        }

        val requestBody =
            requestJson.toString()
                .toRequestBody(
                    "application/json".toMediaType()
                )

        val request =
            Request.Builder()
                .url(
                    "https://openrouter.ai/api/v1/chat/completions"
                )
                .addHeader(
                    "Authorization",
                    "Bearer $apiKey"
                )
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .post(requestBody)
                .build()

        client.newCall(request).execute().use { response ->

            val responseBody =
                response.body?.string().orEmpty()

            if (!response.isSuccessful) {

                throw IllegalStateException(
                    "OpenRouter request failed: " +
                            "${response.code} " +
                            responseBody
                )
            }

            val json =
                JSONObject(responseBody)

            val choices =
                json.optJSONArray("choices")
                    ?: throw IllegalStateException(
                        "OpenRouter returned no choices."
                    )

            if (choices.length() == 0) {
                throw IllegalStateException(
                    "OpenRouter returned an empty response."
                )
            }

            val message =
                choices
                    .getJSONObject(0)
                    .getJSONObject("message")

            message
                .optString("content")
                .trim()
                .also { content ->

                    if (content.isBlank()) {
                        throw IllegalStateException(
                            "OpenRouter returned empty content."
                        )
                    }
                }
        }
    }
}