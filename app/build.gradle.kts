plugins {
    alias(libs.plugins.android.application)
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
        configPath = rootProject.file("scale-demens.yaml")
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
