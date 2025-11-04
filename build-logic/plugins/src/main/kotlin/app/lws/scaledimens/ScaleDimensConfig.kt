package app.lws.scaledimens


class Config @JvmOverloads constructor(
    var config: List<ScaleDimensConfig> = emptyList()
)


class ScaleDimensConfig @JvmOverloads constructor(
    var baseQualifier: String = "",
    var generateQualifier: List<TargetQualifier> = emptyList()
)


class TargetQualifier @JvmOverloads constructor(
    var scale: Float = 0f,
    var qualifier: String = ""
)


