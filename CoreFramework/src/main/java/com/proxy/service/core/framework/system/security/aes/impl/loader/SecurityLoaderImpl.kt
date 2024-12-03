package com.proxy.service.core.framework.system.security.aes.impl.loader

import com.proxy.service.core.framework.system.security.aes.base.action.IDecryptResult
import com.proxy.service.core.framework.system.security.aes.base.action.IEncryptResult
import com.proxy.service.core.framework.system.security.aes.base.action.ISource
import com.proxy.service.core.framework.system.security.aes.base.loader.ILoader
import com.proxy.service.core.framework.system.security.aes.controller.CipherController
import com.proxy.service.core.framework.system.security.aes.impl.action.decrypt.SecurityDecryptSource
import com.proxy.service.core.framework.system.security.aes.impl.action.encrypt.SecurityEncryptSource
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/12/2 20:31
 * @desc:
 */
class SecurityLoaderImpl(
    private val cipher: Cipher,
    private val secretKey: SecretKey,
    private val ivSpec: IvParameterSpec? = null
) : ILoader {
    override fun createEncryptLoader(): ISource<IEncryptResult> {
        val controller = CipherController(
            cipher,
            Cipher.ENCRYPT_MODE,
            secretKey,
            ivSpec
        )
        return SecurityEncryptSource(controller)
    }

    override fun createDecryptLoader(): ISource<IDecryptResult> {
        val controller = CipherController(
            cipher,
            Cipher.DECRYPT_MODE,
            secretKey,
            ivSpec
        )
        return SecurityDecryptSource(controller)
    }
}