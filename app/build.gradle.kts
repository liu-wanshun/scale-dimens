plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("app.lws.scaledimens")
}

android {
    namespace = "app.lws.scaledimens.demo"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }

    scaleDimens {
        baseSw = 360
        generateSwList = intArrayOf(360, 720)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}