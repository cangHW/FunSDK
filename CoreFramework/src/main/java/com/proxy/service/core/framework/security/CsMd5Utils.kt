package com.proxy.service.core.framework.security

import androidx.annotation.WorkerThread
import com.proxy.service.core.framework.log.CsLogger
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2024/4/28 17:31
 * @desc:
 */
object CsMd5Utils {

    /**
     * 对字符串md5加密
     */
    fun getMD5(str: String?): String {
        if (str.isNullOrEmpty()) {
            return ""
        }
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            val md5 = BigInteger(1, md.digest()).toString(16)
            return md5.padStart(32, '0')
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return ""
    }

    /**
     * 获得文件md5
     */
    @WorkerThread
    fun getMD5(file: File?): String {
        if (file == null || !file.exists() || !file.isFile) {
            return ""
        }
        try {
            val buffer = ByteArray(4 * 1024)
            var len: Int
            val md = MessageDigest.getInstance("MD5")
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(file)
                while (fis.read(buffer).also { len = it } != -1) {
                    md.update(buffer, 0, len)
                }
            } finally {
                fis?.close()
            }
            val md5 = BigInteger(1, md.digest()).toString(16)
            return md5.padStart(32, '0')
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return ""
    }

}