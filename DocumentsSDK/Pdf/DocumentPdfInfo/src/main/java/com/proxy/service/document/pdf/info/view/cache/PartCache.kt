package com.proxy.service.document.pdf.info.view.cache

import android.graphics.Bitmap
import android.util.LruCache
import android.util.SparseArray
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.base.enums.CacheType
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.info.view.config.RenderConfig
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2025/6/24 10:02
 * @desc:
 */
class PartCache private constructor(
    maxCount: Int,
    type: CacheType,
    private val config: RenderConfig,
    private val loader: IPdfLoader
) {

    companion object {
        private const val TAG = "${PdfConstants.LOG_TAG_PDF_START}Bitmap"

        fun create(
            maxCount: Int,
            type: CacheType,
            config: RenderConfig,
            loader: IPdfLoader
        ): PartCache {
            return PartCache(maxCount, type, config, loader)
        }
    }

    interface OnRefreshCallback {
        fun onRefresh(position: Int)
    }

    private val isDestroy = AtomicBoolean(false)
    private val sizeCache = SparseArray<Size>()
    private val partCache = LruBitmapCache(maxCount, type)

    fun destroy() {
        isDestroy.set(true)
        partCache.evictAll()
        sizeCache.clear()
    }

    fun getRenderConfig(): RenderConfig {
        return config
    }

    fun getPart(position: Int, width: Int, height: Int, callback: OnRefreshCallback): Part? {
        if (isDestroy.get()) {
            return null
        }

        val count = loader.getTextCount(position)
        val text = loader.getText(position)
        CsLogger.tag(TAG).i("count = $count, text = $text")

        var part = partCache.get(position)
        if (part == null) {
            val size = getBitmapSize(loader, position, width, height)
            val bitmap =
                Bitmap.createBitmap(size.bitmapWidth, size.bitmapHeight, config.format.value)

            val weakCallback = WeakReference(callback)

            CsTask.computationThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    loader.renderPageToBitmap(
                        position,
                        bitmap,
                        renderAnnot = false,
                        autoSize = true,
                        viewBgColor = config.viewBackgroundColor,
                        pageBgColor = config.pageBackgroundColor
                    )
                    return ""
                }
            })?.mainThread()?.doOnNext(object : IConsumer<String> {
                override fun accept(value: String) {
                    if (isDestroy.get()) {
                        return
                    }
                    weakCallback.get()?.onRefresh(position)
                }
            })?.start()
            part = Part(size, bitmap)
            partCache.put(position, part)
        }
        return part
    }

    private fun getBitmapSize(loader: IPdfLoader, position: Int, width: Int, height: Int): Size {
        val size: Size? = sizeCache.get(position)
        if (size != null) {
            return size
        }

        val pageSize = loader.getPageSizePixel(position)
        val scaleW = width * 1.0f / pageSize.widthPixel
        val scaleH = height * 1.0f / pageSize.heightPixel
        val scale = Math.min(scaleW, scaleH)

        val endSize = Size(
            (pageSize.widthPixel * scale).toInt(),
            (pageSize.heightPixel * scale).toInt(),
            pageSize.widthPixel,
            pageSize.heightPixel
        )

        val builder = StringBuilder()
        builder.append("viewSize=").append("${width}x${height}").append(", ")
        builder.append("pageSize=").append(pageSize).append(", ")
        builder.append("scale=").append(scale).append(", ")
        builder.append("endSize=").append("${endSize.bitmapWidth}x${endSize.bitmapHeight}")
        CsLogger.tag(TAG).d(builder.toString())

        sizeCache.put(position, endSize)
        return endSize
    }

    private class LruBitmapCache(maxCount: Int, private val type: CacheType) :
        LruCache<Int, Part>(maxCount) {

        override fun sizeOf(key: Int?, value: Part?): Int {
            if (type == CacheType.SIZE) {
                return value?.bitmap?.byteCount ?: 0
            }
            return 1
        }

        override fun entryRemoved(
            evicted: Boolean,
            key: Int?,
            oldValue: Part?,
            newValue: Part?
        ) {
            oldValue?.bitmap?.let {
                if (!it.isRecycled) {
                    it.recycle()
                }
            }
        }
    }

    data class Size(
        val bitmapWidth: Int,
        val bitmapHeight: Int,
        val pageWidth: Int,
        val pageHeight: Int
    )

    data class Part(val size: Size, val bitmap: Bitmap)
}