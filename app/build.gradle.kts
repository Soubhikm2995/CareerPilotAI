import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

val geminiApiKey =
    localProperties.getProperty("GEMINI_API_KEY", "")

val openRouterApiKey =
    localProperties.getProperty("OPENROUTER_API_KEY", "")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.careerpilot.ai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.careerpilot.ai"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"$geminiApiKey\""
        )

        buildConfigField(
            "String",
            "OPENROUTER_API_KEY",
            "\"$openRouterApiKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.junit.ktx)
    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Unit Test
    testImplementation(libs.junit)

    // Android Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(
        libs.androidx.espresso.core
    )
    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-ai")
    implementation(
        "com.google.firebase:firebase-appcheck-debug"
    )

    // OpenRouter
    implementation(
        "com.squareup.okhttp3:okhttp:5.1.0"
    )

    // PDF
    implementation(libs.pdfbox.android)

    // Kotlin Test
    implementation(kotlin("test"))
}