package app.lws.scaledimens

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.android.build.gradle.api.AndroidSourceDirectorySet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import java.io.File

class ScaleDimensPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AndroidBasePlugin::class.java) {

            val extension =
                project.extensions.create("scaleDimens", ScaleDimensExtension::class.java)

            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)

            val resSourceDirectoriesBySourceSet = mutableMapOf<String, Set<File>>()

            androidComponents.apply {
                onVariants { variant ->
                    val relevantSourcesSets = setOfNotNull(
                        "main",
                        variant.name,
                        variant.buildType,
                        variant.flavorName,
                    ) + variant.productFlavors.map { (_, flavor) ->
                        flavor
                    }

                    val resSourceDirectories = resSourceDirectoriesBySourceSet
                        .mapNotNull { (name, files) ->
                            if (relevantSourcesSets.contains(name)) {
                                files
                            } else {
                                null
                            }
                        }
                        .flatten()

                    variant.sources.res?.let {
                        val addSourceTaskProvider =
                            project.tasks.register<ScaleDimensTask>("scaleDimens${variant.name}") {
                                this.resourceDirectories.set(resSourceDirectories)
                                this.extension.set(extension)
                            }
                        it.addGeneratedSourceDirectory(
                            addSourceTaskProvider,
                            ScaleDimensTask::outputFolder
                        )
                    }
                }
                finalizeDsl { common ->
                    common.sourceSets
                        .map { sourceSet -> sourceSet.name to (sourceSet.res as AndroidSourceDirectorySet).srcDirs }
                        .forEach { resSourceDirectoriesBySourceSet[it.first] = it.second }

                }
            }
        }
    }
}