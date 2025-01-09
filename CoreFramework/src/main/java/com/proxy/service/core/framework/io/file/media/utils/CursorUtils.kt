package com.proxy.service.core.framework.io.file.media.utils

import android.database.Cursor
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/1/7 10:13
 * @desc:
 */
object CursorUtils {

     fun getValueFromCursor(
        cursor: Cursor,
        columnName: String,
        defaultValue: Long
    ): Long {
        try {
            val column: Int = cursor.getColumnIndexOrThrow(columnName)
            return cursor.getLong(column)
        } catch (_: Throwable) {
        }
        return defaultValue
    }

     fun getValueFromCursor(
        cursor: Cursor,
        columnName: String,
        defaultValue: Int
    ): Int {
        try {
            val column: Int = cursor.getColumnIndexOrThrow(columnName)
            return cursor.getInt(column)
        } catch (_: Throwable) {
        }
        return defaultValue
    }

     fun getValueFromCursor(
        cursor: Cursor,
        columnName: String,
        defaultValue: String
    ): String {
        try {
            val column: Int = cursor.getColumnIndexOrThrow(columnName)
            return cursor.getString(column)
        } catch (_: Throwable) {
        }
        return defaultValue
    }

}