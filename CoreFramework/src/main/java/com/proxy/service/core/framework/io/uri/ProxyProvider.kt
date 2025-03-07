package com.proxy.service.core.framework.io.uri

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
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.File
import java.io.FileNotFoundException

/**
 * @author: cangHX
 * @data: 2024/9/20 18:03
 * @desc:
 */
class ProxyProvider : ContentProvider() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Provider"

        private val COLUMNS: Array<String> = arrayOf(
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE
        )

        /**
         * 安全路径
         */
        @GuardedBy("sCache")
        private val SECURITY_PATHS: MutableSet<String> = HashSet()

        @GuardedBy("sCache")
        private val sCache: HashMap<String, PathStrategy> = HashMap()

        /**
         * 添加安全路径
         */
        fun addSecurityPaths(filePath: String) {
            if (TextUtils.isEmpty(filePath)) {
                return
            }
            SECURITY_PATHS.add(filePath)
        }

        /**
         * 移除安全路径
         */
        fun removeSecurityPaths(filePath: String) {
            if (TextUtils.isEmpty(filePath)) {
                return
            }
            HashSet(SECURITY_PATHS).forEach {
                if (it.startsWith(filePath)){
                    SECURITY_PATHS.remove(it)
                }
            }
        }

        /**
         * 获取可供外部使用的 URI
         * */
        fun getUriForFile(file: File?): Uri? {
            if (file == null){
                return null
            }
            val strategy = getPathStrategy("${CsAppUtils.getPackageName()}.proxy_core_provider")
            return strategy.getUriForFile(file)
        }

        private fun getPathStrategy(authority: String): PathStrategy {
            var strategy = sCache[authority]
            if (strategy == null) {
                strategy = SimplePathStrategy(authority)
                sCache[authority] = strategy
            }
            return strategy
        }
    }

    private var mStrategy: PathStrategy? = null

    override fun onCreate(): Boolean {
        return true
    }

    override fun attachInfo(context: Context, info: ProviderInfo) {
        super.attachInfo(context, info)

        if (!info.grantUriPermissions) {
            throw SecurityException("Provider must grant uri permissions")
        }

        mStrategy = SimplePathStrategy(info.authority)
    }

    override fun query(
        uri: Uri,
        projections: Array<String>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
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
            }
        }

        cols = copyOf(cols, i)
        values = copyOf(values, i)

        val cursor = MatrixCursor(cols, 1)
        cursor.addRow(values)
        return cursor
    }

    override fun getType(uri: Uri): String? {
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
        val modeBits = if ("r" == mode) {
            ParcelFileDescriptor.MODE_READ_ONLY
        } else if ("w" == mode || "wt" == mode) {
            (ParcelFileDescriptor.MODE_WRITE_ONLY
                    or ParcelFileDescriptor.MODE_CREATE
                    or ParcelFileDescriptor.MODE_TRUNCATE)
        } else if ("wa" == mode) {
            (ParcelFileDescriptor.MODE_WRITE_ONLY
                    or ParcelFileDescriptor.MODE_CREATE
                    or ParcelFileDescriptor.MODE_APPEND)
        } else if ("rw" == mode) {
            (ParcelFileDescriptor.MODE_READ_WRITE
                    or ParcelFileDescriptor.MODE_CREATE)
        } else if ("rwt" == mode) {
            (ParcelFileDescriptor.MODE_READ_WRITE
                    or ParcelFileDescriptor.MODE_CREATE
                    or ParcelFileDescriptor.MODE_TRUNCATE)
        } else {
            throw IllegalArgumentException("Invalid mode: $mode")
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
                CsLogger.tag(TAG).d(throwable)
            }
            if (TextUtils.isEmpty(path)) {
                return null
            }

            var isPermission = false
            for (securityPath in HashSet(SECURITY_PATHS)) {
                if (path.startsWith(securityPath)) {
                    isPermission = true
                    break
                }
            }

            if (!isPermission) {
                CsLogger.tag(TAG).e("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"CsUriUtils.addProviderResourcePath\" method.")
                return null
            }

            return Uri.Builder().scheme("content").authority(mAuthority).encodedPath(path).build()
        }

        override fun getFileForUri(uri: Uri): File? {
            val path = uri.encodedPath
            CsLogger.tag(TAG).i("path = $path")

            if (TextUtils.isEmpty(path)) {
                return null
            }

            var isPermission = false
            for (securityPath in HashSet(SECURITY_PATHS)) {
                if (path!!.startsWith(securityPath)) {
                    isPermission = true
                    break
                }
            }

            if (!isPermission) {
                CsLogger.tag(TAG).e("The current path is an illegal path and is not allowed to share files，You can try to set it to a safe path by \"CsUriUtils.addProviderResourcePath\" method.")
                return null
            }

            return path?.let { File(it) }
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