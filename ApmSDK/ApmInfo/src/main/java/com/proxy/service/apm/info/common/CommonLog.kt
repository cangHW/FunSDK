package com.proxy.service.apm.info.common

import android.os.Debug
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.utils.MemInfoDocUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.callback.OnSuccessCallback
import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * @author: cangHX
 * @data: 2025/4/15 17:14
 * @desc:
 */
object CommonLog {

    private const val TAG: String = "${Constants.TAG}CommonLog"

    private const val FORMAT_STR_18 = "%-18s %8s"
    private const val FORMAT_STR_35 = "%-35s %2s"

    private const val MIN_TIME: Long = 2
    private const val MAX_TIME: Long = 3

    fun createStream(path: String): OutputStream {
        CsFileUtils.createFile(path)
        return BufferedOutputStream(FileOutputStream(File(path), true))
    }

    fun logAll(path: String, callback: ((Boolean) -> Unit)?) {
        CsTask.ioThread()?.call(object : ICallable<OutputStream> {
            override fun accept(): OutputStream {
                return createStream(path)
            }
        })?.doOnNext(object : IConsumer<OutputStream> {
            override fun accept(value: OutputStream) {
                logMemInfo(value)?.blockGetFirst()
            }
        })?.doOnNext(object : IConsumer<OutputStream> {
            override fun accept(value: OutputStream) {
                logCpuInfo(value)?.blockGetFirst()
            }
        })?.doOnNext(object : IConsumer<OutputStream> {
            override fun accept(value: OutputStream) {
                logLogcat(value)?.blockGetFirst()
            }
        })?.doOnNext(object : IConsumer<OutputStream> {
            override fun accept(value: OutputStream) {
                CsFileUtils.close(value)
            }
        })?.setOnFailedCallback(object : OnFailedCallback {
            override fun onCallback(throwable: Throwable) {
                callback?.invoke(false)
            }
        })?.setOnSuccessCallback(object : OnSuccessCallback<OutputStream> {
            override fun onCallback(value: OutputStream) {
                callback?.invoke(true)
            }
        })?.start()
    }

    /**
     * 输出内存日志
     * */
    fun logMemInfo(outputStream: OutputStream): ITaskOption<String>? {
        return CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val builder = StringBuilder()
                builder.append(Constants.DIVIDER).append("\n")
                builder.append("内存数据").append("\n").append("\n")

                try {
                    val mi = Debug.MemoryInfo()
                    Debug.getMemoryInfo(mi)

                    builder.append("应用信息:").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "Java Heap:",
                            mi.getMemoryStat("summary.java-heap")
                        )
                    ).append(" kB").append("\t\t").append("Java堆内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "Native Heap:",
                            mi.getMemoryStat("summary.native-heap")
                        )
                    ).append(" kB").append("\t\t").append("Native堆内存").append("\n")

                    builder.append(format(FORMAT_STR_18, "Code:", mi.getMemoryStat("summary.code")))
                        .append(" kB").append("\t\t").append("代码内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "Stack:",
                            mi.getMemoryStat("summary.stack")
                        )
                    ).append(" kB").append("\t\t").append("栈内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "Graphics:",
                            mi.getMemoryStat("summary.graphics")
                        )
                    ).append(" kB").append("\t\t").append("图形内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "Private Other:",
                            mi.getMemoryStat("summary.private-other")
                        )
                    ).append(" kB").append("\t\t").append("其他私有内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "System:",
                            mi.getMemoryStat("summary.system")
                        )
                    ).append(" kB").append("\t\t").append("系统内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "TOTAL:",
                            mi.getMemoryStat("summary.total-pss")
                        )
                    ).append(" kB").append("\t\t").append("总PSS内存").append("\n")

                    builder.append(
                        format(
                            FORMAT_STR_18,
                            "TOTAL SWAP:",
                            mi.getMemoryStat("summary.total-swap")
                        )
                    ).append(" kB").append("\t\t").append("总交换内存").append("\n")
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }

                builder.append("\n")
                builder.append("系统信息:").append("\n")

                try {
                    val process =
                        Runtime.getRuntime().exec(arrayOf("sh", "-c", "cat /proc/meminfo"))
                    val reader = BufferedReader(InputStreamReader(process.inputStream))
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        builder.append(format(FORMAT_STR_35, line ?: "", ""))
                            .append(MemInfoDocUtils.findDoc(line ?: ""))
                            .append("\n")
                    }
                    CsFileUtils.close(reader)
                    builder.append("\n").append("\n")
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }

                CsFileWriteUtils.setSourceString(builder.toString()).writeSync(outputStream)
                return ""
            }
        })
    }

    /**
     * 输出 cpu 日志
     * */
    fun logCpuInfo(outputStream: OutputStream): ITaskOption<InputStream>? {
        return CsTask.ioThread()?.call(object : ICallable<InputStream> {
            override fun accept(): InputStream {
                val builder = StringBuilder()
                builder.append(Constants.DIVIDER).append("\n")
                builder.append("cpu").append("\n").append("\n")

                CsFileWriteUtils.setSourceString(builder.toString()).writeSync(outputStream)

                val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", "top -b -n 1"))
                val stream = process.inputStream

                CsTask.ioThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        try {
                            CsFileWriteUtils.setSourceStream(stream)
                                .writeSync(outputStream, shouldThrow = true)
                        } catch (_: Throwable) {
                        }
                        try {
                            process.destroy()
                        } catch (_: Throwable) {
                        }
                        return ""
                    }
                })?.start()
                return stream
            }
        })?.delay(MIN_TIME, TimeUnit.SECONDS)?.doOnNext(object : IConsumer<InputStream> {
            override fun accept(value: InputStream) {
                CsFileUtils.close(value)
                CsFileWriteUtils.setSourceString("\n\n").writeSync(outputStream)
            }
        })
    }

    /**
     * 输出 logcat 日志
     * */
    fun logLogcat(outputStream: OutputStream): ITaskOption<InputStream>? {
        return CsTask.ioThread()?.call(object : ICallable<InputStream> {
            override fun accept(): InputStream {
                val builder = StringBuilder()
                builder.append(Constants.DIVIDER).append("\n")
                builder.append("logcat").append("\n").append("\n")

                CsFileWriteUtils.setSourceString(builder.toString())
                    .writeSync(outputStream)

                val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", "logcat"))
                val stream = process.inputStream

                CsTask.ioThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        try {
                            CsFileWriteUtils.setSourceStream(stream)
                                .writeSync(outputStream, shouldThrow = true)
                        } catch (_: Throwable) {
                        }
                        try {
                            process.destroy()
                        } catch (_: Throwable) {
                        }
                        return ""
                    }
                })?.start()
                return stream
            }
        })?.delay(MAX_TIME, TimeUnit.SECONDS)?.doOnNext(object : IConsumer<InputStream> {
            override fun accept(value: InputStream) {
                CsFileUtils.close(value)
                CsFileWriteUtils.setSourceString("\n\n").writeSync(outputStream)
            }
        })
    }

    private fun format(formatStr: String, name: String, value: String): String {
        return String.format(Locale.US, formatStr, name, value);
    }
}