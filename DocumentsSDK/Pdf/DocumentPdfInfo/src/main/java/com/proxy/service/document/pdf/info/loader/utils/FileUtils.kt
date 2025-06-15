package com.proxy.service.document.pdf.info.loader.utils

import android.net.Uri
import android.os.ParcelFileDescriptor
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.document.pdf.base.constants.Constants
import java.io.File
import java.io.FileDescriptor
import java.lang.reflect.Field

/**
 * @author: cangHX
 * @data: 2025/5/2 21:11
 * @desc:
 */
object FileUtils {

    private const val TAG = "${Constants.LOG_TAG_PDF_START}file"

    private val FD_CLASS: Class<*> = FileDescriptor::class.java
    private const val FD_FIELD_NAME: String = "descriptor"

    private var fdField: Field? = null

    fun getNumFd(file: File): Pair<ParcelFileDescriptor?, Int> {
        if (!CsFileUtils.isFile(file)) {
            return Pair(null, -1)
        }

        try {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            if (fdField == null) {
                fdField = FD_CLASS.getDeclaredField(FD_FIELD_NAME)
                fdField?.isAccessible = true
            }
            val fd = fdField?.getInt(pfd.fileDescriptor) ?: -1
            return Pair(pfd, fd)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return Pair(null, -1)
    }

    fun getNumFd(uri: Uri): Pair<ParcelFileDescriptor?, Int> {
        try {
            val context = CsContextManager.getApplication()
            val pfd = context.contentResolver.openFileDescriptor(uri, "r")
            if (fdField == null) {
                fdField = FD_CLASS.getDeclaredField(FD_FIELD_NAME)
                fdField?.isAccessible = true
            }
            val fd = fdField?.getInt(pfd?.fileDescriptor) ?: -1
            return Pair(pfd, fd)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return Pair(null, -1)
    }

}