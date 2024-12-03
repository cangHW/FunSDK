package com.proxy.service.core.framework.system.security.aes.impl.option

import com.proxy.service.core.framework.system.security.aes.base.option.IPadding
import com.proxy.service.core.framework.system.security.aes.config.Config

/**
 * @author: cangHX
 * @data: 2024/12/2 20:40
 * @desc:
 */
class PaddingImpl<T>(
    private val transformation: String,
    private val result: (transformation: String) -> T
) : IPadding<T> {

    override fun noPadding(): T {
        return result("$transformation/${Config.PADDING_NO}")
    }

    override fun pkcs5padding(): T {
        return result("$transformation/${Config.PADDING_PKCS5}")
    }

    override fun pkcs7padding(): T {
        return result("$transformation/${Config.PADDING_PKCS7}")
    }

    override fun zeroPadding(): T {
        return result("$transformation/${Config.PADDING_ZERO}")
    }

    override fun iso10126padding(): T {
        return result("$transformation/${Config.PADDING_ISO10126}")
    }
}