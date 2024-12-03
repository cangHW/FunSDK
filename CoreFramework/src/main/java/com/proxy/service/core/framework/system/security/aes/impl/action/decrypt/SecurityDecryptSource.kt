package com.proxy.service.core.framework.system.security.aes.impl.action.decrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IDecryptResult
import com.proxy.service.core.framework.system.security.aes.base.action.ISource
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import java.util.Base64

/**
 * @author: cangHX
 * @data: 2024/12/3 10:19
 * @desc:
 */
class SecurityDecryptSource(
    private val controller: IController
) : SecurityDecryptResult(controller), ISource<IDecryptResult> {
    override fun reset() {
        controller.reset()
    }

    override fun setSourceString(str: String): IDecryptResult {
        CsStringUtils.parseHexStr2Byte(str)?.let {
            controller.update(it)
        }
        return this
    }

    override fun setSourceBase64String(base64: String): IDecryptResult {
        CsStringUtils.parseBase64Str2Byte(base64)?.let {
            controller.update(it)
        }
        return this
    }

    override fun setSourceByteArray(byteArray: ByteArray): IDecryptResult {
        controller.update(byteArray)
        return this
    }

    override fun addSourceString(str: String): ByteArray {
        return CsStringUtils.parseHexStr2Byte(str)?.let {
            controller.update(it)
        } ?: ByteArray(0)
    }

    override fun addSourceBase64String(base64: String): ByteArray {
        return CsStringUtils.parseBase64Str2Byte(base64)?.let {
            controller.update(it)
        } ?: ByteArray(0)
    }

    override fun addSourceByteArray(byteArray: ByteArray): ByteArray {
        return controller.update(byteArray)
    }
}