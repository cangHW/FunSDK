package com.proxy.service.core.framework.system.security.aes.impl.option.key

import com.proxy.service.core.framework.system.security.aes.base.loader.ILoader
import com.proxy.service.core.framework.system.security.aes.base.option.IIvParameterSpec
import com.proxy.service.core.framework.system.security.aes.base.option.ISecretKeySpec
import com.proxy.service.core.framework.system.security.aes.impl.option.IvParameterSpecImpl
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * @author: cangHX
 * @data: 2024/12/2 20:34
 * @desc:
 */
class SecretKeySpecNeedIvImpl(
    private val algorithm: String,
    private val transformation: String
) : ISecretKeySpec<IIvParameterSpec<ILoader>> {
    override fun setSecretKeySpec(key: String): IIvParameterSpec<ILoader> {
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), algorithm)
        return IvParameterSpecImpl(cipher, secretKey)
    }

    override fun setSecretKeySpec(byteArray: ByteArray): IIvParameterSpec<ILoader> {
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(byteArray, algorithm)
        return IvParameterSpecImpl(cipher, secretKey)
    }

    override fun setSecretKeySpec(secretKey: SecretKey): IIvParameterSpec<ILoader> {
        val cipher = Cipher.getInstance(transformation)
        return IvParameterSpecImpl(cipher, secretKey)
    }
}