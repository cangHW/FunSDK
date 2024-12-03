package com.proxy.service.core.framework.system.security.aes.impl.option

import com.proxy.service.core.framework.system.security.aes.base.loader.ILoader
import com.proxy.service.core.framework.system.security.aes.base.option.IIvParameterSpec
import com.proxy.service.core.framework.system.security.aes.impl.loader.SecurityLoaderImpl
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/12/2 20:33
 * @desc:
 */
class IvParameterSpecImpl(
    private val cipher: Cipher,
    private val secretKey: SecretKey
) : IIvParameterSpec<ILoader> {
    override fun setIvParameterSpec(spec: String): ILoader {
        val ivSpec = IvParameterSpec(spec.toByteArray(StandardCharsets.UTF_8))
        return SecurityLoaderImpl(cipher, secretKey, ivSpec)
    }

    override fun setIvParameterSpec(byteArray: ByteArray): ILoader {
        val ivSpec = IvParameterSpec(byteArray)
        return SecurityLoaderImpl(cipher, secretKey, ivSpec)
    }

    override fun setIvParameterSpec(ivSpec: IvParameterSpec): ILoader {
        return SecurityLoaderImpl(cipher, secretKey, ivSpec)
    }
}