plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin.api)
    compileOnly("com.android.tools.build:gradle:8.1.0")
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins {
        create("scaleDimensPlugin") {
            id = "app.lws.scaledimens"
            implementationClass = "app.lws.scaledimens.ScaleDimensPlugin"
        }
    }
}
