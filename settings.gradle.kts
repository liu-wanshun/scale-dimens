pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = uri("https://www.jitpack.io/")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
include(":library")