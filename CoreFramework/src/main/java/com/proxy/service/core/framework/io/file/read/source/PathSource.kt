package com.proxy.service.core.framework.io.file.read.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IRead
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

/**
 * @author: cangHX
 * @data: 2024/9/25 10:24
 * @desc:
 */
@RequiresApi(Build.VERSION_CODES.O)
class PathSource(private val path: Path) : IRead {

    companion object{
        private const val TAG = "${CoreConfig.TAG}FileRead_Path"
    }

    /**
     * 读取全部数据
     * */
    override fun readString(charset: Charset): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return ""
        }
        try {
            Files.lines(path, charset).let {
                try {
                    return it.collect(Collectors.joining(System.lineSeparator()))
                }finally {
                    it.close()
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }
}