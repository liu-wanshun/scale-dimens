plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("io.github.liu-wanshun.scale-dimens")
}

android {
    namespace = "app.lws.scaledimens.demo"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }

    scaleDimens {
        baseSw = 360
        generateSwList = intArrayOf(360, 720)
        configPath = rootProject.file("scale-demens.yaml").absolutePath
        //println("configPath = $configPath")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
dependencies {
    implementation(project(mapOf("path" to ":library")))
}
