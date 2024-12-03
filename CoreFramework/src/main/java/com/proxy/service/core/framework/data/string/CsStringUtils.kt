package com.proxy.service.core.framework.data.string

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.Base64

/**
 * 字符串工具
 *
 * @author: cangHX
 * @data: 2024/12/3 11:50
 * @desc:
 */
object CsStringUtils {

    private const val TAG = "${Constants.TAG}String"

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
    fun parseByte2Base64Str(byteArray: ByteArray): String? {
        try {
            return Base64.getEncoder().encodeToString(byteArray)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * base64 字符串转为 byte 数组
     * */
    fun parseBase64Str2Byte(base64: String): ByteArray? {
        try {
            return Base64.getDecoder().decode(base64)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * byte 数组转为 base64 url 字符串
     * */
    fun parseByte2Base64UrlStr(byteArray: ByteArray): String? {
        try {
            return Base64.getUrlEncoder().encodeToString(byteArray)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * base64 url 字符串转为 byte 数组
     * */
    fun parseBase64UrlStr2Byte(base64Url: String): ByteArray? {
        try {
            return Base64.getUrlDecoder().decode(base64Url)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }
}