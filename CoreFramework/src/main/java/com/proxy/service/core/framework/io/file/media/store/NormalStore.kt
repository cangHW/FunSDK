package com.proxy.service.core.framework.io.file.media.store

import android.webkit.MimeTypeMap
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.base.BaseStore
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.framework.io.file.media.utils.CursorUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File


/**
 * @author: cangHX
 * @data: 2025/1/2 10:19
 * @desc:
 */
class NormalStore : BaseStore() {

    init {
        CsLogger.tag(tag).d("NormalStore start")
    }

    override fun insert(callback: InsertCallback?) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val finalSource = source
                if (finalSource == null) {
                    CsLogger.tag(tag).e("source is null")
                    callInsertFailed(callback)
                    return ""
                }

                try {
                    val sourceName = modifyFileNameWithMimeType(displayName, mimeType)
                    val sourceDestFile = File(getRootPath(), "$dirPath${File.separator}$sourceName")

                    if (finalSource.write(sourceDestFile)) {
                        CsLogger.tag(tag).d("insert success")
                        callInsertSuccess(callback, sourceDestFile.absolutePath)
                    } else {
                        CsLogger.tag(tag).e("write error")
                        callInsertFailed(callback)
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                    callInsertFailed(callback)
                }
                return ""
            }
        })?.start()
    }

    override fun query(callback: QueryCallback?) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val finalUri = getUri()
                if (finalUri == null) {
                    CsLogger.tag(tag).e("uri is null")
                    callQueryFailed(callback)
                    return ""
                }

                // 定义要查询的列
                val projection = arrayOf(
                    ID,
                    DISPLAY_NAME,
                    MIME_TYPE,
                    SIZE,
                    DATA,
                    DATE_ADDED,
                    DATE_MODIFIED
                )

                val displayNameConfig = DisplayNameConfig(displayName)
                val mimeTypeConfig = MimeTypeConfig(mimeType)

                // 定义筛选条件
                var selection: String? = displayNameConfig.checkSelection(null)
                var selectionArgs: Array<String>? = displayNameConfig.checkSelectionArgs(null)

                selection = mimeTypeConfig.checkSelection(selection)
                selectionArgs = mimeTypeConfig.checkSelectionArgs(selectionArgs)

                // 定义排序顺序
                val sortOrder = "$DATE_ADDED DESC"
                val contentResolver = CsContextManager.getApplication().contentResolver

                val cursor = contentResolver.query(
                    finalUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )

                if (cursor == null) {
                    CsLogger.tag(tag).e("cursor is null")
                    callQueryFailed(callback)
                    return ""
                }

                val list = ArrayList<DataInfo>()
                cursor.use {
                    while (cursor.moveToNext()) {
                        val mimeType = CursorUtils.getValueFromCursor(cursor, MIME_TYPE, "")
                        if (!mimeTypeConfig.isMatches(mimeType)) {
                            continue
                        }

                        val data = DataInfo()
                        data.id = CursorUtils.getValueFromCursor(cursor, ID, -1L)
                        data.displayName = CursorUtils.getValueFromCursor(cursor, DISPLAY_NAME, "")
                        data.mimeType = mimeType
                        data.size = CursorUtils.getValueFromCursor(cursor, SIZE, 0L)
                        data.rootDir = getRootPath() ?: ""
                        data.path = CursorUtils.getValueFromCursor(cursor, DATA, "")
                        data.dateAdded = CursorUtils.getValueFromCursor(cursor, DATE_ADDED, -1L)
                        data.dateModified =
                            CursorUtils.getValueFromCursor(cursor, DATE_MODIFIED, -1L)
                        data.isTrashed = if (CsFileUtils.isFile(data.path)) {
                            0
                        } else {
                            1
                        }

                        CsLogger.tag(tag).d(data.toString())
                        list.add(data)
                    }
                }
                callQuerySuccess(callback, list)
                return ""
            }
        })?.start()
    }

    /**
     * 修改文件名以匹配给定的 MimeType
     *
     * @param originalFileName 原始文件名
     * @param mimeType         目标 MimeType
     * @return 修改后的文件名
     */
    private fun modifyFileNameWithMimeType(originalFileName: String, mimeType: String): String {
        // 获取原始文件名的不带扩展名部分
        val dotIndex = originalFileName.lastIndexOf(".")
        val baseName = if (dotIndex == -1) {
            originalFileName
        } else {
            originalFileName.substring(0, dotIndex)
        }

        // 获取 MimeType 对应的正确扩展名
        val correctExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

        return if (correctExtension?.trim()?.isNotEmpty() == true) {
            // 返回新的文件名
            "$baseName.$correctExtension"
        } else {
            // 如果未找到合适的扩展名，则返回原始文件名
            originalFileName
        }
    }

}