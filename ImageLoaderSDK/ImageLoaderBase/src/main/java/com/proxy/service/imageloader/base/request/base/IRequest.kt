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
     * 加载网络文件, 支持同一链接指向不同内容
     *
     * @param key   自定义资源唯一标识
     * */
    fun loadUrl(url: String, key: String): R

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