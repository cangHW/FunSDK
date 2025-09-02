package com.proxy.service.core.framework.io.file.media.compat

import android.os.Environment
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.base.AbstractSource
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.media.config.StoreType
import com.proxy.service.core.framework.io.file.media.source.ByteSource
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/1/3 11:19
 * @desc:
 */
class VideoStoreCompat : AbstractSource<IMediaStore.IVideo>(), IMediaStore.IVideo {

    init {
        store.setStoreType(StoreType.VIDEO)
        store.setDisplayName("video_${System.currentTimeMillis()}")
        store.setMimeType(MimeType.VIDEO_MP4)
        store.setDir(Environment.DIRECTORY_MOVIES + File.separator + CsAppUtils.getAppName())
    }

    /**
     * 设置源数据
     * */
    override fun setSourceByte(bytes: ByteArray): IMediaStore.IVideo {
        store.setSource(ByteSource(bytes))
        return getT()
    }

    override fun getT(): IMediaStore.IVideo {
        return this
    }
}