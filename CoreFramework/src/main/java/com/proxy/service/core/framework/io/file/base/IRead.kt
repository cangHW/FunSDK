package com.proxy.service.core.framework.io.file.base

import java.nio.charset.Charset

/**
 * @author: cangHX
 * @data: 2024/9/24 10:09
 * @desc:
 */
interface IRead {

    /**
     * 读取全部数据
     * */
    fun readString(charset: Charset = Charsets.UTF_8): String

}