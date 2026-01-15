package com.proxy.service.core.framework.io.uri.utils

import android.content.Context
import android.content.res.XmlResourceParser
import android.text.TextUtils
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
    private const val XML_FILE_NAME = "cs_proxy_provider"

    private const val XML_TAG_FILES_PATH = "proxy-files-path"
    private const val XML_TAG_CACHE_PATH = "proxy-cache-path"
    private const val XML_TAG_EXTERNAL_FILES_PATH = "proxy-external-files-path"
    private const val XML_TAG_EXTERNAL_CACHE_PATH = "proxy-external-cache-path"
    private const val XML_TAG_CUSTOM_PATH = "proxy-custom-path"

    private const val XML_TAG_ATTR_NAME = "name"
    private const val XML_TAG_ATTR_PATH = "path"

    fun parser(context: Context, logTag: String): ArrayList<SharePathInfo> {
        val list = ArrayList<SharePathInfo>()

        var parser: XmlResourceParser? = null
        try {
            val resources = context.resources
            val id = resources.getIdentifier(XML_FILE_NAME, "xml", context.packageName)
            if (id == 0) {
                CsLogger.tag(logTag).w("The default shared path has not been configured.")
                return list
            }

            parser = resources.getXml(id)
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                try {
                    if (eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    parserAttr(context, logTag, parser)?.let {
                        list.add(it)
                    }
                } finally {
                    eventType = parser.next()
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(logTag).e(throwable)
        } finally {
            parser?.close()
        }

        return list
    }

    private fun parserAttr(
        context: Context,
        logTag: String,
        parser: XmlResourceParser
    ): SharePathInfo? {
        var pathInfo: SharePathInfo? = null

        when (parser.name) {
            XML_TAG_FILES_PATH -> {
                val prefix = context.filesDir.absolutePath
                pathInfo = createSharePath(parser, logTag, parserDirPath(prefix))
            }

            XML_TAG_CACHE_PATH -> {
                val prefix = context.cacheDir.absolutePath
                pathInfo = createSharePath(parser, logTag, parserDirPath(prefix))
            }

            XML_TAG_EXTERNAL_FILES_PATH -> {
                val prefix = context.getExternalFilesDir(null)?.absolutePath
                if (prefix != null) {
                    pathInfo = createSharePath(parser, logTag, parserDirPath(prefix))
                } else {
                    CsLogger.tag(logTag)
                        .w("The current device does not support external paths. tag=${parser.name}")
                }
            }

            XML_TAG_EXTERNAL_CACHE_PATH -> {
                val prefix = context.externalCacheDir?.absolutePath
                if (prefix != null) {
                    pathInfo = createSharePath(parser, logTag, parserDirPath(prefix))
                } else {
                    CsLogger.tag(logTag)
                        .w("The current device does not support external paths. tag=${parser.name}")
                }
            }

            XML_TAG_CUSTOM_PATH -> {
                pathInfo = createSharePath(parser, logTag, "")
            }
        }
        return pathInfo
    }

    private fun createSharePath(
        parser: XmlResourceParser,
        logTag: String,
        prefix: String
    ): SharePathInfo? {
        val name = parser.getAttributeValue(null, XML_TAG_ATTR_NAME)
        val path = parser.getAttributeValue(null, XML_TAG_ATTR_PATH)

        if (path.isNullOrEmpty() || path.isBlank() || path == "." || path == File.separator) {
            if (TextUtils.isEmpty(prefix)) {
                CsLogger.tag(logTag)
                    .e("There is a lack of meaningful paths. name=$name, path=$path, prefix=$prefix")
                return null
            }
            return SharePathInfo.create(name, prefix)
        }

        if (TextUtils.isEmpty(prefix)) {
            return SharePathInfo.create(name, path)
        }

        var tempPath = File(prefix, path).absolutePath
        if (path.endsWith(File.separator)) {
            tempPath = "$tempPath${File.separator}"
        }

        return SharePathInfo.create(name, tempPath)
    }

    private fun parserDirPath(path: String): String {
        if (TextUtils.isEmpty(path)) {
            return ""
        }
        if (path.endsWith(File.separator)) {
            return path
        }
        return "$path${File.separator}"
    }
}