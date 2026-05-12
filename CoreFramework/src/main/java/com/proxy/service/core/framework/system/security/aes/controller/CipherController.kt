package com.proxy.service.core.framework.system.security.aes.controller

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import com.proxy.service.core.framework.system.security.aes.config.Config
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @date: 2024/12/3 10:50
 * @desc:
 */
class CipherController(
    private val cipher: Cipher,
    private val opMode: Int,
    private val secretKey: SecretKey,
    private val ivSpec: IvParameterSpec? = null
) : IController {

    private val byteArrayList = ArrayList<ByteArray>()
    private var hasError = false

    init {
        reset()
    }

    override fun reset() {
        byteArrayList.clear()
        hasError = false
        try {
            if (ivSpec == null) {
                cipher.init(opMode, secretKey)
            } else {
                cipher.init(opMode, secretKey, ivSpec)
            }
        } catch (throwable: Throwable) {
            hasError = true
            CsLogger.tag(Config.TAG).e(throwable)
        }
    }

    override fun update(byteArray: ByteArray): ByteArray {
        if (hasError) {
            return ByteArray(0)
        }
        try {
            val array = cipher.update(byteArray)
            if (array != null) {
                byteArrayList.add(array)
                return array
            }
        } catch (throwable: Throwable) {
            hasError = true
            CsLogger.tag(Config.TAG).e(throwable)
        }
        return ByteArray(0)
    }

    override fun updateWithOutCache(byteArray: ByteArray): ByteArray {
        if (hasError) {
            return ByteArray(0)
        }
        try {
            return cipher.update(byteArray)
        } catch (throwable: Throwable) {
            hasError = true
            CsLogger.tag(Config.TAG).e(throwable)
        }
        return ByteArray(0)
    }

    override fun finish(): ByteArray {
        if (hasError) {
            return ByteArray(0)
        }
        try {
            val array = cipher.doFinal()
            if (array != null) {
                byteArrayList.add(array)
            }
            return mergeByteArrays(byteArrayList)
        } catch (throwable: Throwable) {
            hasError = true
            CsLogger.tag(Config.TAG).e(throwable)
        }
        return ByteArray(0)
    }

    override fun isError(): Boolean = hasError

    private fun mergeByteArrays(byteArrayList: List<ByteArray>): ByteArray {
        val totalSize = byteArrayList.map { it.size }.sum()
        val result = ByteArray(totalSize)
        var currentPosition = 0
        for (byteArray in byteArrayList) {
            System.arraycopy(byteArray, 0, result, currentPosition, byteArray.size)
            currentPosition += byteArray.size
        }
        return result
    }

}
