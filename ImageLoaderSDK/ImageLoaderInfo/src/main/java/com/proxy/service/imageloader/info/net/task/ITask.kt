package com.proxy.service.imageloader.info.net.task

import java.io.Closeable
import java.io.IOException
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2025/10/14 16:23
 * @desc:
 */
interface ITask : Closeable {

    fun isSuccessful(): Boolean

    @Throws(IOException::class)
    fun bodyByteStream(): InputStream

    fun contentType(): String?

    fun error(): String?
}