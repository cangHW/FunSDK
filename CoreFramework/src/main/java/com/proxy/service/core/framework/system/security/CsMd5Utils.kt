package com.proxy.service.core.framework.system.security

import androidx.annotation.WorkerThread
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * 安全，md5 相关工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:31
 * @desc:
 */
object CsMd5Utils {

    private const val TAG = "${Constants.TAG}Md5"

    /**
     * 对字符串进行 md5 加密
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
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

    /**
     * 对文件进行 md5 加密
     */
    @WorkerThread
    fun getMD5(file: File?): String {
        if (file == null || !file.exists() || !file.isFile) {
            return ""
        }
        try {
            val buffer = ByteArray(8 * 1024)
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
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

}