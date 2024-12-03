package com.proxy.service.core.framework.system.security.aes.base.loader

import com.proxy.service.core.framework.system.security.aes.base.action.IDecryptResult
import com.proxy.service.core.framework.system.security.aes.base.action.IEncryptResult
import com.proxy.service.core.framework.system.security.aes.base.action.ISource

/**
 * @author: cangHX
 * @data: 2024/12/2 19:40
 * @desc:
 */
interface ILoader {

    /**
     * 创建加密加载器
     * */
    fun createEncryptLoader(): ISource<IEncryptResult>

    /**
     * 创建解密加载器
     * */
    fun createDecryptLoader(): ISource<IDecryptResult>

}