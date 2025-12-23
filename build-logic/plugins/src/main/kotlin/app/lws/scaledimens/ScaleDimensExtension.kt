package app.lws.scaledimens

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class ScaleDimensExtension @Inject constructor(
    private val objects: ObjectFactory
) {

    val configPath: RegularFileProperty = objects.fileProperty()
}
