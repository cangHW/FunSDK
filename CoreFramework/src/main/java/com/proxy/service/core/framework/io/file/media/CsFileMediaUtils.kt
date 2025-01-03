package com.proxy.service.core.framework.io.file.media

import android.os.Environment
import android.provider.MediaStore
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.compat.AudioStoreCompat
import com.proxy.service.core.framework.io.file.media.compat.ImageStoreCompat
import com.proxy.service.core.framework.io.file.media.compat.QueryStoreCompat
import com.proxy.service.core.framework.io.file.media.compat.VideoStoreCompat

/**
 * 兼容 Android 高版本 MediaStore 操作
 *
 * @author: cangHX
 * @data: 2024/12/31 11:51
 * @desc:
 */
object CsFileMediaUtils {

    /**
     * 获取媒体图片管理器
     * */
    fun getImageManager(): IMediaStore.IImage {
        return ImageStoreCompat()
    }

    /**
     * 获取媒体视频管理器
     * */
    fun getVideoManager(): IMediaStore.IVideo {
        return VideoStoreCompat()
    }

    /**
     * 获取媒体音频管理器
     * */
    fun getAudioManager(): IMediaStore.IAudio {
        return AudioStoreCompat()
    }

    /**
     * 获取媒体查询管理器
     * */
    fun getQueryManager(): IMediaStore.IQuery {
        return QueryStoreCompat()
    }

}