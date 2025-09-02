package com.proxy.service.core.framework.system.security.md5

import androidx.annotation.WorkerThread
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.math.BigInteger
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * 安全相关 —— md5 工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:31
 * @desc:
 */
object CsMd5Utils {

    private const val ALGORITHM_MD5 = "MD5"
    private const val TAG = "${CoreConfig.TAG}Md5"


    /**
     * 对字符串进行 md5 加密
     */
    fun getMD5(str: String?): String {
        if (str.isNullOrEmpty()) {
            return ""
        }
        try {
            val md = MessageDigest.getInstance(ALGORITHM_MD5)
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
        if (!CsFileUtils.isFile(file)) {
            return ""
        }

        try {
            FileInputStream(file).use { fis ->
                return getMD5(fis)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

    /**
     * 对流里面的内容进行 md5 加密
     */
    @WorkerThread
    fun getMD5(inputStream: InputStream?): String {
        if (inputStream == null) {
            return ""
        }
        try {
            val buffer = ByteArray(8 * 1024)
            val digest = MessageDigest.getInstance(ALGORITHM_MD5)

            DigestInputStream(inputStream, digest).use { dis ->
                while (dis.read(buffer) > 0) {
                    // No operation needed inside loop
                }
            }

            val md5 = BigInteger(1, digest.digest()).toString(16)
            return md5.padStart(32, '0')
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }
}