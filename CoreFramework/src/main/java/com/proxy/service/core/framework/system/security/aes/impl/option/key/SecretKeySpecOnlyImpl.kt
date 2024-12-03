package com.proxy.service.core.framework.system.security.aes.impl.option.key

import com.proxy.service.core.framework.system.security.aes.base.loader.ILoader
import com.proxy.service.core.framework.system.security.aes.base.option.ISecretKeySpec
import com.proxy.service.core.framework.system.security.aes.impl.loader.SecurityLoaderImpl
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * @author: cangHX
 * @data: 2024/12/2 20:34
 * @desc:
 */
class SecretKeySpecOnlyImpl(
    private val algorithm: String,
    private val transformation: String
) : ISecretKeySpec<ILoader> {
    override fun setSecretKeySpec(key: String): ILoader {
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), algorithm)
        return SecurityLoaderImpl(cipher, secretKey)
    }

    override fun setSecretKeySpec(byteArray: ByteArray): ILoader {
        val cipher = Cipher.getInstance(transformation)
        val secretKey = SecretKeySpec(byteArray, algorithm)
        return SecurityLoaderImpl(cipher, secretKey)
    }

    override fun setSecretKeySpec(secretKey: SecretKey): ILoader {
        val cipher = Cipher.getInstance(transformation)
        return SecurityLoaderImpl(cipher, secretKey)
    }
}