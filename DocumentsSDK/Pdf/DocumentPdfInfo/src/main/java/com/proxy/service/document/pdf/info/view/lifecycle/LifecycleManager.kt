package com.proxy.service.document.pdf.info.view.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.info.view.cache.PartCache

/**
 * @author: cangHX
 * @data: 2025/5/14 15:48
 * @desc:
 */
class LifecycleManager private constructor() {

    companion object {
        val instance by lazy { LifecycleManager() }
    }

    fun bindLifecycle(owner: LifecycleOwner?, loader: IPdfLoader, cache: PartCache) {
        owner?.lifecycle?.addObserver(LifecycleObserverImpl(loader, cache))
    }

    private class LifecycleObserverImpl(
        private val loader: IPdfLoader,
        private val cache: PartCache
    ) : DefaultLifecycleObserver {

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            owner.lifecycle.removeObserver(this)
            loader.destroy()
            cache.destroy()
        }
    }

}