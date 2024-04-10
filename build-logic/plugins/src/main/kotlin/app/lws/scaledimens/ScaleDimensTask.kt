package app.lws.scaledimens

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

abstract class ScaleDimensTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputFolder: DirectoryProperty


    @get:Input
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
        val originDimens = mutableMapOf<String, Node>()
        baseResDirs.forEach { it ->
            collectDimens(originDimens, File(it, "values-sw${baseSw}dp"))
        }
        val originDimensSet = originDimens.values
        // 生成缩放后的dimens
        generateSwList.forEach { targetSw ->
            // 如果sw路径已经存在,不需要生成
            if (targetSw != baseSw) {
                generateSwFile(baseSw, originDimensSet, targetSw)
            }
        }
        // 获取默认的原始dimens
        val originDefaultDimens = mutableMapOf<String, Node>()
        baseResDirs.forEach { it ->
            originDefaultDimens.putAll(collectDimens(originDimens, File(it, "values")))
        }
        val originDefaultDimensSet = originDefaultDimens.values
        // 生成缩放后的dimens
        generateSwList.forEach { targetSw ->
            generateSwFile(baseSw, originDefaultDimensSet, targetSw)
        }
    }

    private fun collectDimens(
        originDimens: MutableMap<String, Node>,
        dir: File
    ): MutableMap<String, Node> {
        val currentDimens = mutableMapOf<String, Node>()
        if (dir.exists() && dir.isDirectory) {
            val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val db: DocumentBuilder = dbf.newDocumentBuilder()
            dir.listFiles()?.forEach { file ->
                val document: Document = db.parse(file)
                val nodeList: NodeList = document.getElementsByTagName("dimen")
                for (i in 0 until nodeList.length) {
                    val node: Node = nodeList.item(i)
                    val dimensName = node.attributes.item(0).nodeValue
                    if (originDimens[dimensName] == null) {
                        originDimens[dimensName] = node
                        currentDimens[dimensName] = node
                    } else {
                        logger.log(
                            LogLevel.DEBUG,
                            "已经添加过dimensName=${dimensName}，忽略来自${dir}的dimens"
                        )
                    }
                }
            }
        }
        return currentDimens
    }


    private fun generateSwFile(baseSw: Int, originDimens: MutableCollection<Node>, targetSw: Int) {

        val targetFile =
            File(outputFolder.asFile.get(), "values-sw${targetSw}dp/values.xml")
        targetFile.parentFile.mkdirs()
        val scale = targetSw / baseSw.toFloat()

        val targetValue = StringBuffer()
        targetValue.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        targetValue.append("<resources>\n")

        originDimens.forEach { node ->
            val attrs: NamedNodeMap = node.attributes
            val childNodes: NodeList = node.childNodes
            val nodeValue: String = childNodes.item(0).nodeValue
            if (nodeValue.contains("dp") || nodeValue.contains("sp")) {
                val attr: Node = attrs.item(0)
                val elementStrBuilder = StringBuilder()
                elementStrBuilder.append("<dimen " + attr.nodeName + "=\"" + attr.nodeValue + "\">")
                if (nodeValue.contains("dp")) {
                    val value = java.lang.Float.valueOf(nodeValue.replace("dp", ""))
                    elementStrBuilder.append((value * scale).toString() + "dp")
                } else if (nodeValue.contains("sp")) {
                    val value = java.lang.Float.valueOf(nodeValue.replace("sp", ""))
                    elementStrBuilder.append((value * scale).toString() + "sp")
                }
                elementStrBuilder.append("</dimen>")
                targetValue.append(
                    """
                    $elementStrBuilder

                    """.trimIndent()
                )
            }
        }
        targetValue.append("</resources>")
        targetFile.writeText(targetValue.toString())
    }


}