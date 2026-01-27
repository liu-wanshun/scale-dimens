# scale-dimens

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.liu-wanshun.scale-dimens)](https://plugins.gradle.org/plugin/io.github.liu-wanshun.scale-dimens)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

      A gradle plugin for generate scaled dimens

## Compatibility

|     | Minimum version | 
|-----|----------------:|
| AGP |           8.4.0 |  
| JDK |              17 |  

## Add Gradle Plugin

Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):


```kotlin
pluginManagement {
   repositories {
      gradlePluginPortal()
   }
}

plugins {
    id("io.github.liu-wanshun.scale-dimens") version "2.0.1"
}
```


<details>
  <summary>Using <code>apply plugin</code> (the old way) </summary>



[legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application)

[Learn how to apply plugins to subprojects](https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl)

```kotlin
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("io.github.liu-wanshun.scale-dimens:plugins:2.0.1")
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
