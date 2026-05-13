package com.proxy.service.core.framework.system.security.sha

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author: cangHX
 * @date: 2026/5/13 15:53
 * @desc:
 */
object CsSha256Utils {

    private const val ALGORITHM_SHA256 = "SHA-256"
    private const val TAG = "${CoreConfig.TAG}Sha256"

    /**
     * 对字符串进行 sha256 加密
     */
    fun get(str: String?): String {
        if (str.isNullOrEmpty()) {
            return ""
        }
        return get(str.toByteArray())
    }

    /**
     * 对 byte 数组进行 sha256 加密
     */
    fun get(bytes: ByteArray?): String {
        if (bytes == null) {
            return ""
        }
        try {
            val md = MessageDigest.getInstance(ALGORITHM_SHA256)
            md.update(bytes)
            val hex = BigInteger(1, md.digest()).toString(16)
            return hex.padStart(64, '0')
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }
}