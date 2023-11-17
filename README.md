# scale-dimens

[![jitpack](https://jitpack.io/v/io.github.liu-wanshun/scale-dimens.svg)](https://jitpack.io/#io.github.liu-wanshun/scale-dimens)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

      A gradle plugin for generate scaled dimens

## Add Gradle Plugin

Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):

```kotlin
pluginManagement {
   repositories {
      maven {
         url = uri("https://www.jitpack.io/")
      }
   }
}

plugins {
    id("io.github.liu-wanshun.scale-dimens") version "<latest version>"
}
```

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):

```kotlin
buildscript {
    repositories {
        maven {
            url = uri("https://www.jitpack.io/")
        }
    }
    dependencies {
        classpath("io.github.liu-wanshun.scale-dimens:plugins:<latest version>")
    }
}

apply(plugin = "io.github.liu-wanshun.scale-dimens")
```

[Learn how to apply plugins to subprojects](https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl)

## Example for config plugin

```kotlin
scaleDimens {
    baseSw = 360
    generateSwList = intArrayOf(360, 720)
}
```