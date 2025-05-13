package com.proxy.service.apihttp.info.upload.worker.body

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Sink
import okio.buffer


/**
 * @author: cangHX
 * @data: 2024/12/19 20:24
 * @desc:
 */
class ProgressRequestBody(private val body: RequestBody) : RequestBody() {

    override fun contentType(): MediaType? {
        return body.contentType()
    }

    override fun contentLength(): Long {
        return body.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        val bufferedSink: BufferedSink = getSink(sink).buffer()
        body.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    private fun getSink(sink: BufferedSink): Sink {
        return object : ForwardingSink(sink) {
            private var bytesWritten: Long = 0
            private var contentLength = -1L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == -1L) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
            }
        }
    }

}