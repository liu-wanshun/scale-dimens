package app.lws.scaledimens

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.android.build.gradle.api.AndroidSourceDirectorySet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import java.io.File

class ScaleDimensPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType<AndroidBasePlugin> {

            val extension =
                project.extensions.create<ScaleDimensExtension>("scaleDimens")

            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)

            val resSourceDirectoriesBySourceSet = mutableMapOf<String, Set<File>>()

            androidComponents.apply {

                finalizeDsl { common ->
                    common.sourceSets
                        .map { sourceSet -> sourceSet.name to (sourceSet.res as AndroidSourceDirectorySet).srcDirs }
                        .forEach { resSourceDirectoriesBySourceSet[it.first] = it.second }

                }

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

                    val addSourceTaskProvider =
                        project.tasks.register<ScaleDimensTask>("scaleDimens${variant.name}") {
                            group = "scale-dimens"
                            this.resourceDirectories.set(resSourceDirectories)
                            this.extension.set(extension)
                        }
                    variant.sources.res?.addGeneratedSourceDirectory(
                        addSourceTaskProvider,
                        ScaleDimensTask::outputFolder
                    )
                }
            }
        }
    }
}
