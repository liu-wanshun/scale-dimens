plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle-api:7.4.0")
    compileOnly("com.android.tools.build:gradle:7.4.0")
    implementation(gradleKotlinDsl())
    implementation("org.yaml:snakeyaml:2.5")
}

version = "1.0.6-SNAPSHOT"
gradlePlugin {
    plugins {
        create("scaleDimensPlugin") {
            id = "io.github.liu-wanshun.scale-dimens"
            implementationClass = "app.lws.scaledimens.ScaleDimensPlugin"
        }
    }
}
