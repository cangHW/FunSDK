package com.proxy.service.core.framework.data.string

import android.util.Base64
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * 字符串工具
 *
 * @author: cangHX
 * @data: 2024/12/3 11:50
 * @desc:
 */
object CsStringUtils {

    private const val TAG = "${CoreConfig.TAG}String"

    /**
     * byte 数组转为 16 进制字符串
     * */
    fun parseByte2HexStr(byteArray: ByteArray): String? {
        try {
            val hexString = StringBuilder()
            for (b in byteArray) {
                val hex = Integer.toHexString(0xFF and b.toInt())
                if (hex.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hex)
            }
            return hexString.toString()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * 16 进制字符串转为 byte 数组
     * */
    fun parseHexStr2Byte(hexStr: String): ByteArray? {
        try {
            return ByteArray(hexStr.length / 2) { index ->
                val byteIndex = index * 2
                val high = Character.digit(hexStr[byteIndex], 16)
                val low = Character.digit(hexStr[byteIndex + 1], 16)
                ((high shl 4) + low).toByte()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * byte 数组转为 base64 字符串
     * */
    fun parseByte2Base64Str(byteArray: ByteArray, flags: Int = Base64.DEFAULT): String? {
        try {
            return Base64.encodeToString(byteArray, flags)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * base64 字符串转为 byte 数组
     * */
    fun parseBase64Str2Byte(base64: String, flags: Int = Base64.DEFAULT): ByteArray? {
        try {
            return Base64.decode(base64, flags)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * byte 数组转为 base64 url 字符串
     * */
    fun parseByte2Base64UrlStr(byteArray: ByteArray, flags: Int = Base64.DEFAULT): String? {
        try {
            return Base64.encodeToString(byteArray, flags)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * base64 url 字符串转为 byte 数组
     * */
    fun parseBase64UrlStr2Byte(base64Url: String, flags: Int = Base64.DEFAULT): ByteArray? {
        try {
            return Base64.decode(base64Url, flags)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }
}