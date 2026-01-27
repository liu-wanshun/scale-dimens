import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("com.gradle.plugin-publish") version "2.0.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        apiVersion = KotlinVersion.KOTLIN_1_9
        languageVersion = KotlinVersion.KOTLIN_1_9
    }
    coreLibrariesVersion = "1.9.25"
}

dependencies {
    compileOnly("com.android.tools.build:gradle-api:8.4.0")
    compileOnly(gradleKotlinDsl())
    implementation("org.yaml:snakeyaml:2.5")
}

group = "io.github.liu-wanshun"
version = "2.0.1"
gradlePlugin {
    website = "https://github.com/liu-wanshun/scale-dimens"
    vcsUrl = "https://github.com/liu-wanshun/scale-dimens"
    plugins {
        create("scaleDimensPlugin") {
            id = "io.github.liu-wanshun.scale-dimens"
            implementationClass = "app.lws.scaledimens.ScaleDimensPlugin"
            displayName = "scale-dimens plugin"
            description = "Gradle plugin to generate scaled dimens"
            tags.set(listOf("android", "dimens"))
        }
    }
}
