package com.proxy.service.core.framework.io.uri.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.annotation.GuardedBy
import com.proxy.service.api.log.LogInit
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.uri.info.SharePathInfo
import com.proxy.service.core.framework.io.uri.utils.XmlParserUtils
import java.io.File
import java.io.FileNotFoundException

/**
 * @author: cangHX
 * @data: 2024/9/20 18:03
 * @desc: 如果需要配置默认共享路径可以按以下方式进行配置.
 *
 * 在项目的 src/main/res/xml 文件夹下(如果不存在对应文件夹则自行创建)创建 cs_proxy_provider.xml 文件, 文件内容如下:
 * <paths> // 固定标签
 *
 *     <!-- 共享应用的私有文件目录, 可选 -->
 *     <proxy-files-path // 可选标签, 标识当前配置为应用的私有文件目录
 *         name="internal_file" // 不能为空, 自定义名称, 用于替换对应路径的名称, 用来隐藏真实的路径, 提高安全性
 *         path="." /> // 准备共享的路径, 如果为 <空>、<.>、</>, 则使用标签对应的路径, 如果不为空则与标签对应路径进行拼接, 如果以 / 结尾则代表共享文件夹, 否则代表共享对应文件
 *
 *     <!-- 共享应用的私有缓存目录, 可选 -->
 *     <proxy-cache-path // 同上
 *         name="internal_cache" // 同上
 *         path=".." /> // 同上
 *
 *     <!-- 共享外部存储的应用私有文件目录, 可选 -->
 *     <proxy-external-files-path // 同上
 *         name="external_file" // 同上
 *         path="Pictures/" /> // 同上
 *
 *     <!-- 共享外部存储的应用私有缓存目录, 可选 -->
 *     <proxy-external-cache-path // 同上
 *         name="external_cache" // 同上
 *         path="Pictures/" /> // 同上
 *
 *     <!-- 共享自定义目录, 可选 -->
 *     <proxy-custom-path // 可选标签, 标识当前配置为自定义目录
 *         name="custom_file" // 同上
 *         path="/storage/emulated/0/" /> // 准备共享的路径, 不能为空, 需要是待共享的全路径
 *
 * </paths>
 *
 */
class ProxyProvider : ContentProvider() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}ProxyProvider"

        private const val AUTHORITY_SUFFIX = ".proxy_share_file_provider"

        /**
         * 资源最后更新时间
         * */
        private const val LAST_MODIFIED = "_last_modified"

        private val COLUMNS: Array<String> = arrayOf(
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE,
            LAST_MODIFIED
        )

        /**
         * 安全路径
         */
        @GuardedBy("sCache")
        private val SECURITY_PATHS = LinkedHashMap<String, SharePathInfo>()

        @GuardedBy("sCache")
        private val sCache: HashMap<String, PathStrategy> = HashMap()

        /**
         * 添加安全路径
         */
        fun addSecurityPaths(name: String, filePath: String) {
            if (TextUtils.isEmpty(filePath)) {
                return
            }
            if (TextUtils.isEmpty(name)) {
                return
            }
            synchronized(sCache) {
                addSharePathInfoToMap(SharePathInfo.create(name, filePath))
            }
        }

        /**
         * 移除安全路径
         */
        fun removeSecurityPaths(filePath: String) {
            if (TextUtils.isEmpty(filePath)) {
                return
            }
            synchronized(sCache) {
                LinkedHashMap(SECURITY_PATHS).values.forEach {
                    if (it.path == filePath) {
                        SECURITY_PATHS.remove(it.name)
                    }
                }
            }
        }

        /**
         * 获取可供外部使用的 URI
         * */
        fun getUriForFile(file: File?): Uri? {
            if (file == null) {
                return null
            }
            val strategy = getPathStrategy(
                CsContextManager.getApplication(),
                "${CsAppUtils.getPackageName()}$AUTHORITY_SUFFIX"
            )
            return strategy.getUriForFile(file)
        }

        private fun getPathStrategy(context: Context, authority: String): PathStrategy {
            var strategy: PathStrategy?
            synchronized(sCache) {
                strategy = sCache[authority]
                if (strategy == null) {
                    strategy = SimplePathStrategy(authority)
                    sCache[authority] = strategy!!

                    XmlParserUtils.parser(context, TAG).forEach {
                        addSharePathInfoToMap(it)
                    }
                }
            }
            return strategy!!
        }

        private fun addSharePathInfoToMap(sharePathInfo: SharePathInfo) {
            if (SECURITY_PATHS.containsKey(sharePathInfo.name)) {
                val info = SECURITY_PATHS[sharePathInfo.name]
                if (info?.isSame(sharePathInfo) == true) {
                    return
                }
                throw SecurityException("Provider does not allow the configuration of security path policies with the same name. $sharePathInfo, $info")
            }
            CsLogger.tag(TAG).i("Add a secure sharing path. $sharePathInfo")
            SECURITY_PATHS[sharePathInfo.name] = sharePathInfo
        }
    }

    private var mStrategy: PathStrategy? = null

    override fun onCreate(): Boolean {
//        LogInit.setIsDebug(true)
        return true
    }

    override fun attachInfo(context: Context, info: ProviderInfo) {
        super.attachInfo(context, info)

        if (!info.grantUriPermissions) {
            throw SecurityException("Provider must grant uri permissions")
        }

        mStrategy = getPathStrategy(context, info.authority)
    }

    override fun query(
        uri: Uri,
        projections: Array<String>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor {
        var projection = projections
        val file = mStrategy?.getFileForUri(uri)

        if (projection == null) {
            projection = COLUMNS
        }

        var cols = arrayOfNulls<String>(projection.size)
        var values = arrayOfNulls<Any>(projection.size)
        var i = 0
        for (col in projection) {
            if (OpenableColumns.DISPLAY_NAME == col) {
                cols[i] = OpenableColumns.DISPLAY_NAME
                values[i++] = file?.name
            } else if (OpenableColumns.SIZE == col) {
                cols[i] = OpenableColumns.SIZE
                values[i++] = file?.length()
            } else if (LAST_MODIFIED == col) {
                cols[i] = LAST_MODIFIED
                values[i++] = file?.lastModified()
            }
        }

        cols = copyOf(cols, i)
        values = copyOf(values, i)

        val cursor = MatrixCursor(cols, 1)
        cursor.addRow(values)
        return cursor
    }

    override fun getType(uri: Uri): String {
        val file = mStrategy?.getFileForUri(uri)

        val lastDot = file?.name?.lastIndexOf('.') ?: -1
        if (lastDot >= 0) {
            val extension = file?.name?.substring(lastDot + 1)
            val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (mime != null) {
                return mime
            }
        }

        return "application/octet-stream"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String?>?): Int {
        val file = mStrategy?.getFileForUri(uri)
        return if (file?.delete() == true) 1 else 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String?>?
    ): Int {
        return 0
    }

    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val file = mStrategy?.getFileForUri(uri)
        if (file == null || !file.exists()) {
            CsLogger.tag(TAG).e("file is null. ${uri.encodedPath}")
            return null
        }
        val fileMode = modeToMode(mode)
        return ParcelFileDescriptor.open(file, fileMode)
    }

    private fun modeToMode(mode: String): Int {
        val modeBits = when (mode) {
            "r" -> {
                ParcelFileDescriptor.MODE_READ_ONLY
            }

            "w", "wt" -> {
                (ParcelFileDescriptor.MODE_WRITE_ONLY
                        or ParcelFileDescriptor.MODE_CREATE
                        or ParcelFileDescriptor.MODE_TRUNCATE)
            }

            "wa" -> {
                (ParcelFileDescriptor.MODE_WRITE_ONLY
                        or ParcelFileDescriptor.MODE_CREATE
                        or ParcelFileDescriptor.MODE_APPEND)
            }

            "rw" -> {
                (ParcelFileDescriptor.MODE_READ_WRITE
                        or ParcelFileDescriptor.MODE_CREATE)
            }

            "rwt" -> {
                (ParcelFileDescriptor.MODE_READ_WRITE
                        or ParcelFileDescriptor.MODE_CREATE
                        or ParcelFileDescriptor.MODE_TRUNCATE)
            }

            else -> {
                throw IllegalArgumentException("Invalid mode: $mode")
            }
        }
        return modeBits
    }

    interface PathStrategy {
        fun getUriForFile(file: File): Uri?

        fun getFileForUri(uri: Uri): File?
    }

    private class SimplePathStrategy(private val mAuthority: String) : PathStrategy {
        override fun getUriForFile(file: File): Uri? {
            var path = ""
            try {
                path = file.canonicalPath
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }

            if (TextUtils.isEmpty(path)) {
                return null
            }

            var sharePathInfo: SharePathInfo? = null
            for (value in LinkedHashMap(SECURITY_PATHS).values) {
                if (value.isMatchedWithEncode(path)) {
                    sharePathInfo = value
                    break
                }
            }

            if (sharePathInfo == null) {
                CsLogger.tag(TAG)
                    .e("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"CsUriManager.addProviderResourcePath\" method.")
                return null
            }

            try {
                path = sharePathInfo.encode(path)
                val tempFile = File(path)
                path = File(tempFile.parentFile, Uri.encode(tempFile.name)).canonicalPath
                return Uri.Builder().scheme("content")
                    .authority(mAuthority)
                    .encodedPath(path)
                    .build()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return null
        }

        override fun getFileForUri(uri: Uri): File? {
            var path = uri.encodedPath
            CsLogger.tag(TAG).i("FileForUri. path = $path")

            if (path.isNullOrEmpty()) {
                return null
            }

            val tempFile = File(path)
            path = File(tempFile.parentFile, Uri.decode(tempFile.name)).canonicalPath

            var sharePathInfo: SharePathInfo? = null
            for (value in LinkedHashMap(SECURITY_PATHS).values) {
                if (value.isMatchedWithDecode(path ?: "")) {
                    sharePathInfo = value
                    break
                }
            }

            if (sharePathInfo == null) {
                CsLogger.tag(TAG)
                    .e("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"CsUriManager.addProviderResourcePath\" method.")
                return null
            }

            try {
                path = sharePathInfo.decode(path ?: "")
                return File(path)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return null
        }
    }

    private fun copyOf(original: Array<String?>, newLength: Int): Array<String?> {
        val result = arrayOfNulls<String>(newLength)
        System.arraycopy(original, 0, result, 0, newLength)
        return result
    }

    private fun copyOf(original: Array<Any?>, newLength: Int): Array<Any?> {
        val result = arrayOfNulls<Any>(newLength)
        System.arraycopy(original, 0, result, 0, newLength)
        return result
    }

}