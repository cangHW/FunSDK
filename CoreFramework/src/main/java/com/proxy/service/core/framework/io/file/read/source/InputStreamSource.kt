package com.proxy.service.core.framework.io.file.read.source

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.base.IRead
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.Channels
import java.nio.charset.Charset

/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
open class InputStreamSource(protected val stream: InputStream) : IRead {

    companion object {
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
        } finally {
            CsFileUtils.close(stream)
        }
        return ""
    }

    override fun readLines(charset: Charset): List<String> {
        return try {
            stream.bufferedReader(charset).use { reader ->
                reader.readLines()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            emptyList()
        } finally {
            CsFileUtils.close(stream)
        }
    }

    override fun readBytes(): ByteArray {
        return try {
            stream.readBytes()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            ByteArray(0)
        } finally {
            CsFileUtils.close(stream)
        }
    }


}