package com.proxy.service.camera.base.callback

import java.io.File

/**
 * @author: cangHX
 * @data: 2026/2/5 15:33
 * @desc:
 */
interface TakePictureCallback {

    /**
     * 拦截图片保存
     *
     * @param bytes     图片数据
     * @param file      图片准备保存的位置
     *
     * @return 返回 true 代表拦截本次保存操作
     * */
    fun interceptPhotoSave(bytes: ByteArray, file: File): Boolean {
        return false
    }

    /**
     * 成功
     *
     * @param filePath 图片地址
     * */
    fun onSuccess(filePath: String)

    /**
     * 失败
     * */
    fun onFailed()

}