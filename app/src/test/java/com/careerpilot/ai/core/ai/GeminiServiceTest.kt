package com.careerpilot.ai.core.ai

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GeminiServiceTest {

    @Test
    fun geminiGeneratesResponse() = runBlocking {

        val service = GeminiService()

        val response = service.generateText(
            "Explain what an ATS resume is in one short sentence."
        )

        assertTrue(response.isNotBlank())
    }
}