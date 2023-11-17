plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
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
            id = "io.github.liu-wanshun.scale-dimens"
            implementationClass = "app.lws.scaledimens.ScaleDimensPlugin"
        }
    }
}
