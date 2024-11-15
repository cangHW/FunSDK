package com.proxy.service.core.framework.io.file.read.impl

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IRead
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.charset.Charset

/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
open class InputStreamSource(protected val stream: InputStream) : IRead {

    private val tag = "${Constants.TAG}FileRead_InputStream"

    /**
     * 读取全部数据
     * */
    override fun readString(charset: Charset): String {
        try {
            val content = StringBuilder()
            Channels.newChannel(stream).use { channel ->
                val buffer = ByteBuffer.allocate(Constants.IO_BUFFER_SIZE)

                while (channel.read(buffer) > 0) {
                    buffer.flip()
                    content.append(charset.decode(buffer))
                    buffer.clear()
                }
            }
            return content.toString()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return ""
    }


}