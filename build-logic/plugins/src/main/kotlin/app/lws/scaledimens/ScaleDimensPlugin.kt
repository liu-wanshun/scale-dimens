package app.lws.scaledimens

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class ScaleDimensPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType<AndroidBasePlugin> {

            val extension = project.extensions.create<ScaleDimensExtension>("scaleDimens")

            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)

            androidComponents.onVariants { variant ->
                val res = variant.sources.res ?: return@onVariants
                val addSourceTaskProvider =
                    project.tasks.register<ScaleDimensTask>("scaleDimens${variant.name.capitalize()}") {
                        group = "scale-dimens"
                        this.resourceDirectories.setFrom(res.static)
                        this.configs.convention(extension.configPath)
                    }
                res.addGeneratedSourceDirectory(
                    addSourceTaskProvider, ScaleDimensTask::outputFolder
                )
            }
        }
    }
}
