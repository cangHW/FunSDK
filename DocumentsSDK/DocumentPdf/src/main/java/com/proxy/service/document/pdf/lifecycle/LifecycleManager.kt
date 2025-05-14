package com.proxy.service.document.pdf.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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

    fun bindLifecycle(owner: LifecycleOwner?, loader: PdfLoader) {
        owner?.lifecycle?.addObserver(LifecycleObserverImpl(owner, loader))
    }

    private class LifecycleObserverImpl(
        private val owner: LifecycleOwner,
        private val loader: PdfLoader
    ) : DefaultLifecycleObserver {

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            owner.lifecycle.removeObserver(this)
            loader.destroy()
        }
    }

}