package com.proxy.service.core.framework.io.file.media.compat

import android.provider.MediaStore
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.base.AbstractMedia

/**
 * @author: cangHX
 * @data: 2025/1/2 15:50
 * @desc:
 */
class QueryStoreCompat : AbstractMedia<IMediaStore.IQuery>(), IMediaStore.IQuery {

    init {
        store.setUri(MediaStore.Files.getContentUri("external"))
    }

    override fun getT(): IMediaStore.IQuery {
        return this
    }
}