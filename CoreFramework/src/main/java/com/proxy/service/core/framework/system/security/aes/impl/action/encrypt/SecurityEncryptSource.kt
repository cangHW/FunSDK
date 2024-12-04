package com.proxy.service.core.framework.system.security.aes.impl.action.encrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IEncryptResult
import com.proxy.service.core.framework.system.security.aes.base.action.IInstantRun
import com.proxy.service.core.framework.system.security.aes.base.action.ISource
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import java.nio.charset.StandardCharsets

/**
 * @author: cangHX
 * @data: 2024/12/3 10:19
 * @desc:
 */
class SecurityEncryptSource(
    private val controller: IController
) : SecurityEncryptResult(controller), ISource<IEncryptResult> {
    override fun reset() {
        controller.reset()
    }

    override fun setSourceString(str: String): IEncryptResult {
        controller.update(str.toByteArray(StandardCharsets.UTF_8))
        return this
    }

    override fun setSourceBase64String(base64: String): IEncryptResult {
        CsStringUtils.parseBase64Str2Byte(base64)?.let {
            controller.update(it)
        }
        return this
    }

    override fun setSourceByteArray(byteArray: ByteArray): IEncryptResult {
        controller.update(byteArray)
        return this
    }

    override fun startInstantRun(): IInstantRun {
        return SecurityEncryptInstantRun(controller)
    }
}