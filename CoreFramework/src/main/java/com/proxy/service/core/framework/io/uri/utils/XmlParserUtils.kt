package com.proxy.service.core.framework.io.uri.utils

import android.content.Context
import android.content.res.XmlResourceParser
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.resource.CsResUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.uri.info.SharePathInfo
import org.xmlpull.v1.XmlPullParser
import java.io.File

/**
 * @author: cangHX
 * @data: 2026/1/10 18:19
 * @desc:
 */
object XmlParserUtils {

    // CsProxyProvider.xml
    private const val XML_FILE_NAME = "CsProxyProvider"

    private const val XML_TAG_FILES_PATH = "proxy-files-path"
    private const val XML_TAG_CACHE_PATH = "proxy-cache-path"
    private const val XML_TAG_EXTERNAL_FILES_PATH = "proxy-external-files-path"
    private const val XML_TAG_EXTERNAL_CACHE_PATH = "proxy-external-cache-path"

    private const val XML_TAG_ATTR_NAME = "name"
    private const val XML_TAG_ATTR_PATH = "path"

    fun parser(context: Context, logTag: String): ArrayList<SharePathInfo> {
        val list = ArrayList<SharePathInfo>()

        var parser: XmlResourceParser? = null
        try {
            val id = CsResUtils.getXmlIdByName(XML_FILE_NAME)
            if (id == 0) {
                CsLogger.tag(logTag).w("The default shared path has not been configured.")
                return list
            }

            parser = context.resources.getXml(id)
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                try {
                    if (eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    parserAttr(logTag, parser)?.let {
                        list.add(it)
                    }
                } finally {
                    eventType = parser.next()
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(logTag).e(throwable)
        } finally {
            // 关闭解析器
            parser?.close()
        }

        return list
    }

    private fun parserAttr(logTag: String, parser: XmlResourceParser): SharePathInfo? {
        var pathInfo: SharePathInfo? = null

        when (parser.name) {
            XML_TAG_FILES_PATH -> {
                val name = parser.getAttributeValue(null, XML_TAG_ATTR_NAME)
                val path = parser.getAttributeValue(null, XML_TAG_ATTR_PATH)

                val prefix = CsContextManager.getApplication().filesDir.absolutePath
                pathInfo = SharePathInfo.create(name, File(prefix, path).absolutePath)
            }

            XML_TAG_CACHE_PATH -> {
                val name = parser.getAttributeValue(null, XML_TAG_ATTR_NAME)
                val path = parser.getAttributeValue(null, XML_TAG_ATTR_PATH)

                val prefix = CsContextManager.getApplication().cacheDir.absolutePath
                pathInfo = SharePathInfo.create(name, File(prefix, path).absolutePath)
            }

            XML_TAG_EXTERNAL_FILES_PATH -> {
                val name = parser.getAttributeValue(null, XML_TAG_ATTR_NAME)
                val path = parser.getAttributeValue(null, XML_TAG_ATTR_PATH)

                val prefix =
                    CsContextManager.getApplication().getExternalFilesDir(null)?.absolutePath
                pathInfo = SharePathInfo.create(name, File(prefix, path).absolutePath)
            }

            XML_TAG_EXTERNAL_CACHE_PATH -> {
                val name = parser.getAttributeValue(null, XML_TAG_ATTR_NAME)
                val path = parser.getAttributeValue(null, XML_TAG_ATTR_PATH)

                val prefix = CsContextManager.getApplication().externalCacheDir?.absolutePath
                pathInfo = SharePathInfo.create(name, File(prefix, path).absolutePath)
            }
        }

        pathInfo?.let {
            CsLogger.tag(logTag).e("Tag: $it")
        }
        return pathInfo
    }

}