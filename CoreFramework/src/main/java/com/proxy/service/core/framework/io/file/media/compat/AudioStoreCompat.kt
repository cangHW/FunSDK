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
 * @data: 2025/1/3 11:23
 * @desc:
 */
class AudioStoreCompat : AbstractSource<IMediaStore.IAudio>(), IMediaStore.IAudio {

    init {
        store.setStoreType(StoreType.AUDIO)
        store.setDisplayName("audio_${System.currentTimeMillis()}")
        store.setMimeType(MimeType.AUDIO_MPEG)
        store.setDir(Environment.DIRECTORY_MUSIC + File.separator + CsAppUtils.getAppName())
    }

    /**
     * 设置源数据
     * */
    override fun setSourceByte(bytes: ByteArray): IMediaStore.IAudio {
        store.setSource(ByteSource(bytes))
        return getT()
    }

    override fun getT(): IMediaStore.IAudio {
        return this
    }
}