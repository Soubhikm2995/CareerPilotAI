package com.careerpilot.ai.di

import com.careerpilot.ai.core.ai.AiProvider
import com.careerpilot.ai.core.ai.AiRouter
import com.careerpilot.ai.core.ai.GeminiService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindAiProvider(
        aiRouter: AiRouter
    ): AiProvider
}