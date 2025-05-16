package com.proxy.service.document.pdf.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.document.base.pdf.loader.IPdfLoader
import com.proxy.service.document.pdf.loader.impl.PdfLoader

/**
 * @author: cangHX
 * @data: 2025/5/14 15:48
 * @desc:
 */
class LifecycleManager private constructor() {

    companion object {
        val instance by lazy { LifecycleManager() }
    }

    fun bindLifecycle(owner: LifecycleOwner?, loader: IPdfLoader) {
        owner?.lifecycle?.addObserver(LifecycleObserverImpl(loader))
    }

    private class LifecycleObserverImpl(
        private val loader: IPdfLoader
    ) : DefaultLifecycleObserver {

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            owner.lifecycle.removeObserver(this)
            loader.destroy()
        }
    }

}