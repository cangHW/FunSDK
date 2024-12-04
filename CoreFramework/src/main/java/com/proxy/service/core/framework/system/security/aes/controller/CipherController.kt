package com.proxy.service.core.framework.system.security.aes.controller

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import com.proxy.service.core.framework.system.security.aes.config.Config
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/12/3 10:50
 * @desc:
 */
class CipherController(
    private val cipher: Cipher,
    private val opMode: Int,
    private val secretKey: SecretKey,
    private val ivSpec: IvParameterSpec? = null
) : IController {

    private val byteArrayList = ArrayList<ByteArray>()

    init {
        byteArrayList.clear()
        reset()
    }

    override fun reset() {
        try {
            if (ivSpec == null) {
                cipher.init(opMode, secretKey)
            } else {
                cipher.init(opMode, secretKey, ivSpec)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
        }
    }

    override fun update(byteArray: ByteArray): ByteArray {
        try {
            val array = cipher.update(byteArray)
            byteArrayList.add(array)
            return array
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
        }
        return ByteArray(0)
    }

    override fun updateWithOutCache(byteArray: ByteArray): ByteArray {
        try {
            return cipher.update(byteArray)
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
        }
        return ByteArray(0)
    }

    override fun finish(): ByteArray {
        try {
            byteArrayList.add(cipher.doFinal())
            return mergeByteArrays(byteArrayList)
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
        }
        return ByteArray(0)
    }

    private fun mergeByteArrays(byteArrayList: List<ByteArray>): ByteArray {
        val totalSize = byteArrayList.sumOf { it.size }
        val result = ByteArray(totalSize)
        var currentPosition = 0
        for (byteArray in byteArrayList) {
            System.arraycopy(byteArray, 0, result, currentPosition, byteArray.size)
            currentPosition += byteArray.size
        }
        return result
    }

}