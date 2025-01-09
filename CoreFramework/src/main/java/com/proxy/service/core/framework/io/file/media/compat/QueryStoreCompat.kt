package com.proxy.service.core.framework.io.file.media.compat

import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.base.AbstractMedia
import com.proxy.service.core.framework.io.file.media.config.StoreType

/**
 * @author: cangHX
 * @data: 2025/1/2 15:50
 * @desc:
 */
class QueryStoreCompat : AbstractMedia<IMediaStore.IQuery>(), IMediaStore.IQuery {

    init {
        store.setStoreType(StoreType.FILE)
    }

    override fun getT(): IMediaStore.IQuery {
        return this
    }
}