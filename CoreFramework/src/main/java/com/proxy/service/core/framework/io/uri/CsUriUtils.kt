package com.proxy.service.core.framework.io.uri

import android.net.Uri
import android.os.Build
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.File

/**
 * Uri 操作工具
 *
 * @author: cangHX
 * @data: 2024/9/23 14:18
 * @desc:
 */
object CsUriUtils {

    private const val TAG = "${Constants.TAG}Uri"

    /**
     * 添加允许通过 provider 共享的文件路径，用于对外提供资源 Uri 等
     * 如果不设置，默认所有路径都是非安全路径，建议设置
     * */
    fun addProviderResourcePath(filePath: String) {
        CsLogger.tag(TAG).i("add Security Path. : $filePath")
        ProxyProvider.addSecurityPaths(filePath)
    }

    /**
     * 移除通过 provider 共享的文件路径，移除当前路径及其子路径
     * */
    fun removeProviderResourcePath(filePath: String) {
        CsLogger.tag(TAG).i("remove Security Path. : $filePath")
        ProxyProvider.removeSecurityPaths(filePath)
    }

    /**
     * 获取文件 uri
     * */
    fun getUriByPath(path: String?): Uri? {
        if (path == null) {
            return null
        }
        return getUriByFile(File(path))
    }

    /**
     * 获取文件 uri
     * */
    fun getUriByFile(file: File?): Uri? {
        if (!CsFileUtils.isFile(file)) {
            return null
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file)
        }
        return ProxyProvider.getUriForFile(file)
    }

}