plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    //id("io.github.liu-wanshun.scale-dimens")
}

android {
    namespace = "app.lws.scaledimens.libdemo"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

//scaleDimens {
//    baseSw = 360
//    generateSwList = intArrayOf(360, 720)
//}