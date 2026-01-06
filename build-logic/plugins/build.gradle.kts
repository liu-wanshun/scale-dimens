import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("com.gradle.plugin-publish") version "2.0.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle-api:7.4.0")
    compileOnly("com.android.tools.build:gradle:7.4.0")
    implementation(gradleKotlinDsl())
    implementation("org.yaml:snakeyaml:2.5")
}

group = "io.github.liu-wanshun"
version = "2.0.0"
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
