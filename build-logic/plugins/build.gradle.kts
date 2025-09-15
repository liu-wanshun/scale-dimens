plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin.api)
    compileOnly(libs.android.gradlePlugin.release)
    implementation(gradleKotlinDsl())
}

version = "1.0.4-SNAPSHOT"
gradlePlugin {
    plugins {
        create("scaleDimensPlugin") {
            id = "io.github.liu-wanshun.scale-dimens"
            implementationClass = "app.lws.scaledimens.ScaleDimensPlugin"
        }
    }
}
