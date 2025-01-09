package com.proxy.service.core.framework.io.file.media.base

import android.media.MediaScannerConnection
import android.os.Build
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.media.store.NormalStore
import com.proxy.service.core.framework.io.file.media.store.SupportV29Store


/**
 * @author: cangHX
 * @data: 2025/1/2 10:11
 * @desc:
 */
abstract class AbstractMedia<T> : IMediaStore<T>, IMediaStore.IInsertAction,
    IMediaStore.IQueryAction {

    protected val tag = "${CoreConfig.TAG}MediaStore_Media"

    protected var store: BaseStore = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        SupportV29Store()
    } else {
        NormalStore()
    }

    override fun setDisplayName(displayName: String): T {
        if (displayName.trim().isEmpty()){
            CsLogger.tag(tag).d("displayName can not be null or empty.")
        }else {
            store.setDisplayName(displayName)
        }
        return getT()
    }

    override fun setMimeType(mimeType: MimeType): T {
        store.setMimeType(mimeType)
        return getT()
    }

    override fun insert(callback: InsertCallback?) {
        store.insert(object : InsertCallback {
            override fun onSuccess(path: String) {
                MediaScannerConnection.scanFile(
                    CsContextManager.getApplication(),
                    arrayOf(path),
                    null, null
                )
                callback?.onSuccess(path)
            }

            override fun onFailed() {
                callback?.onFailed()
            }
        })
    }

    override fun query(callback: QueryCallback?) {
        store.query(callback)
    }

    /**
     * 获取当前对象
     * */
    protected abstract fun getT(): T
}