package app.lws.scaledimens

interface ScaleDimensExtension {

    @Deprecated("Please use configPath")
    var baseSw: Int

    @Deprecated("Please use configPath")
    var generateSwList: IntArray

    var configPath: String
}
