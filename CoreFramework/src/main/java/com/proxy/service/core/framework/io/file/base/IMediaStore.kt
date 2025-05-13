package com.proxy.service.core.framework.io.file.base

import android.graphics.Bitmap
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType

/**
 * @author: cangHX
 * @data: 2024/12/31 14:03
 * @desc:
 */
interface IMediaStore<T> {

    /**
     * 设置名称
     * */
    fun setDisplayName(displayName: String): T

    /**
     * 设置类型
     * */
    fun setMimeType(mimeType: MimeType): T

    interface IInsertAction {
        /**
         * 插入
         * */
        fun insert(callback: InsertCallback?)
    }

    interface IQueryAction {
        /**
         * 查询
         * */
        fun query(callback: QueryCallback?)
    }



    interface IImageSource<T> {
        /**
         * 设置源数据
         * */
        fun setSourceBitmap(bitmap: Bitmap): T

        /**
         * 设置源数据
         * */
        fun setSourceByte(bytes: ByteArray): T
    }

    /**
     * 媒体-图片
     * */
    interface IImage : IMediaStore<IImage>, ISource<IImage>, IImageSource<IImage>, IInsertAction



    interface IVideoSource<T> {

        /**
         * 设置源数据
         * */
        fun setSourceByte(bytes: ByteArray): T
    }

    /**
     * 媒体-视频
     * */
    interface IVideo : IMediaStore<IVideo>, ISource<IVideo>, IVideoSource<IVideo>, IInsertAction



    interface IAudioSource<T> {

        /**
         * 设置源数据
         * */
        fun setSourceByte(bytes: ByteArray): T
    }

    /**
     * 媒体-音频
     * */
    interface IAudio : IMediaStore<IAudio>, ISource<IAudio>, IAudioSource<IAudio>, IInsertAction



    /**
     * 媒体-查询
     * */
    interface IQuery : IMediaStore<IQuery>, IQueryAction
}