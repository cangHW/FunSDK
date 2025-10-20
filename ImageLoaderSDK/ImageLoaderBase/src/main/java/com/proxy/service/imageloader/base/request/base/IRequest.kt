package com.proxy.service.imageloader.base.request.base

/**
 * 资源加载器
 *
 * @author: cangHX
 * @data: 2024/5/15 21:02
 * @desc:
 */
interface IRequest<R> {

    /**
     * 加载网络文件
     * */
    fun loadUrl(url: String): R

    /**
     * 加载本地文件
     * */
    fun loadPath(path: String): R

    /**
     * 加载 assets 目录文件.
     * 例如：asd.png 或者 dir/asd.png
     * */
    fun loadAsset(fileName: String): R

}