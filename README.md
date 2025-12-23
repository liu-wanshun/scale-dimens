# scale-dimens

[![jitpack](https://jitpack.io/v/io.github.liu-wanshun/scale-dimens.svg)](https://jitpack.io/#io.github.liu-wanshun/scale-dimens)
[![Weekly download statistics](https://jitpack.io/v/io.github.liu-wanshun/scale-dimens/week.svg)](https://jitpack.io/#io.github.liu-wanshun/scale-dimens)
[![Monthly download statistics](https://jitpack.io/v/io.github.liu-wanshun/scale-dimens/month.svg)](https://jitpack.io/#io.github.liu-wanshun/scale-dimens)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

      A gradle plugin for generate scaled dimens

## Compatibility

|     | Minimum version | 
|-----|----------------:|
| AGP |           7.4.0 |  
| JDK |              11 |  

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
    id("io.github.liu-wanshun.scale-dimens") version "2.0.0-SNAPSHOT"
}
```


<details>
  <summary>Using <code>apply plugin</code> (the old way) </summary>



[legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application)

[Learn how to apply plugins to subprojects](https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl)

```kotlin
buildscript {
    repositories {
        maven {
            url = uri("https://www.jitpack.io/")
        }
    }
    dependencies {
        classpath("io.github.liu-wanshun.scale-dimens:plugins:2.0.0-SNAPSHOT")
    }
}

apply(plugin = "io.github.liu-wanshun.scale-dimens")
```

</details>





## Example for config plugin

Kotlin DSL / Groovy DSL :

```kotlin
scaleDimens {
    configPath = rootProject.file("scale-demens.yaml")
}
```

Example configPath file : [scale-demens.yaml](scale-demens.yaml)
