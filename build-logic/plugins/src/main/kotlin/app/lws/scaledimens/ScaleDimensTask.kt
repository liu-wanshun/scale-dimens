package app.lws.scaledimens

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

abstract class ScaleDimensTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputFolder: DirectoryProperty


    @get:InputFiles
    abstract val resourceDirectories: ConfigurableFileCollection

    @get:InputFile
    abstract val configs: RegularFileProperty


    @TaskAction
    fun taskAction() {
        outputFolder.asFile.get().deleteRecursively()

        val baseResDirs = resourceDirectories.files
        if (baseResDirs.isEmpty()) {
            return
        }

        val input: InputStream = FileInputStream(configs.asFile.get())

        val configData = Yaml().loadAs(input, Config::class.java)

        logger.log(LogLevel.WARN, "list =  ${configData.javaClass} $configData ")

        val list = configData.config

        for (config in list) {
            // 获取原始dimens
            val baseDimens = mutableMapOf<String, Node>()
            baseResDirs.forEach { it ->
                collectDimens(File(it, "values-${config.baseQualifier}"), baseDimens)
                collectDimens(File(it, "values"), baseDimens)
            }
            logger.log(
                LogLevel.INFO,
                "collectDimens ${config.baseQualifier} done, size ${baseDimens.size} "
            )
            // 生成缩放后的dimens
            config.generateQualifier.forEach { targetQualifier ->
                generateSwFile(baseResDirs, baseDimens.values, targetQualifier)
            }
        }
    }

    private fun collectDimens(dir: File, destination: MutableMap<String, Node>) {
        if (dir.exists() && dir.isDirectory) {
            val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val db: DocumentBuilder = dbf.newDocumentBuilder()
            dir.listFiles()?.forEach { file ->
                val document: Document = db.parse(file)
                val nodeList: NodeList = document.getElementsByTagName("dimen")
                for (i in 0 until nodeList.length) {
                    val node: Node = nodeList.item(i)
                    val dimensName = node.attributes.item(0).nodeValue
                    if (destination[dimensName] == null) {
                        destination[dimensName] = node
                        logger.log(LogLevel.DEBUG, "find dimens = $dimensName from $dir")
                    } else {
                        logger.log(
                            LogLevel.DEBUG,
                            "already added dimensName=$dimensName,ignore this from $dir"
                        )
                    }
                }
            }
        } else {
            logger.log(LogLevel.INFO, "can't find $dir")
        }
    }


    private fun generateSwFile(
        baseResDirs: Set<File>,
        originDimens: MutableCollection<Node>,
        targetQualifier: TargetQualifier
    ) {

        val qualifier = targetQualifier.qualifier
        val targetDimens = mutableMapOf<String, Node>()
        baseResDirs.forEach { it ->
            collectDimens(File(it, "values-$qualifier"), targetDimens)
        }


        val targetFile =
            File(outputFolder.asFile.get(), "values-$qualifier/values.xml")
        targetFile.parentFile.mkdirs()
        val document: Document =
            if (!targetFile.exists()) {
                targetFile.createNewFile()
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
            } else {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(targetFile)
            }
        val scale = targetQualifier.scale

        val resources = document.getElementsByTagName("resources").item(0)
            ?: document.createElement("resources").also { document.appendChild(it) }

        originDimens.forEach { node ->
            val attrs: NamedNodeMap = node.attributes
            val textContent: String = node.textContent
            if (textContent.endsWith("dp") || textContent.endsWith("sp")) {
                val attr: Node = attrs.item(0)
                if (targetDimens[attr.nodeValue] != null) {
                    logger.log(
                        LogLevel.DEBUG,
                        "origin already has dimens = ${attr.nodeValue} ,not need generate"
                    )
                    return@forEach
                }
                val dimen = document.createElement("dimen")
                dimen.setAttribute(attr.nodeName, attr.nodeValue)
                val scaleValue = java.lang.Float.valueOf(
                    textContent.substring(0, textContent.length - 2)
                ) * scale
                dimen.textContent =
                    scaleValue.toString() + textContent.substring(textContent.length - 2)

                resources.appendChild(dimen)
            }
        }
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.transform(DOMSource(document), StreamResult(targetFile))
    }


}
