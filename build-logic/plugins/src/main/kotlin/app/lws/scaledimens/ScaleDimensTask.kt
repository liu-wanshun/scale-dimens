package app.lws.scaledimens

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
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
    abstract val resourceDirectories: SetProperty<File>

    @get:Input
    abstract val extension: Property<ScaleDimensExtension>


    @TaskAction
    fun taskAction() {
        outputFolder.asFile.get().deleteRecursively()

        val baseSw: Int = extension.get().baseSw
        if (baseSw == 0) {
            return
        }

        val baseResDirs = resourceDirectories.get()
        if (baseResDirs.isEmpty()) {
            return
        }

        val generateSwList: IntArray = extension.get().generateSwList

        if (generateSwList.isEmpty()) {
            return
        }

        // 获取原始dimens
        val baseDimens = mutableMapOf<String, Node>()
        baseResDirs.forEach { it ->
            collectDimens(File(it, "values-sw${baseSw}dp"), baseDimens)
            collectDimens(File(it, "values"), baseDimens)
        }
        logger.log(LogLevel.INFO, "collectDimens done, size ${baseDimens.size} ")
        // 生成缩放后的dimens
        generateSwList.forEach { targetSw ->
            generateSwFile(baseResDirs, baseSw, baseDimens.values, targetSw)
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
        baseSw: Int,
        originDimens: MutableCollection<Node>,
        targetSw: Int
    ) {

        val targetDimens = mutableMapOf<String, Node>()
        baseResDirs.forEach { it ->
            collectDimens(File(it, "values-sw${targetSw}dp"), targetDimens)
        }


        val targetFile =
            File(outputFolder.asFile.get(), "values-sw${targetSw}dp/values.xml")
        targetFile.parentFile.mkdirs()
        val document: Document =
            if (!targetFile.exists()) {
                targetFile.createNewFile()
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
            } else {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(targetFile)
            }
        val scale = targetSw / baseSw.toFloat()

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
