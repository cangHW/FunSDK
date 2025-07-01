package com.proxy.service.core.framework.io.file.read.source

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IRead
import com.proxy.service.core.framework.io.file.config.IoConfig
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

    companion object{
        private const val TAG = "${CoreConfig.TAG}FileRead_InputStream"
    }

    /**
     * 读取全部数据
     * */
    override fun readString(charset: Charset): String {
        try {
            val content = StringBuilder()
            Channels.newChannel(stream).use { channel ->
                val buffer = ByteBuffer.allocate(IoConfig.IO_BUFFER_SIZE)

                while (channel.read(buffer) > 0) {
                    buffer.flip()
                    content.append(charset.decode(buffer))
                    buffer.clear()
                }
            }
            return content.toString()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }


}