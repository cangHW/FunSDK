package com.proxy.service.imageloader.info.utils

/**
 * @author: cangHX
 * @date: 2025/10/16 15:46
 * @desc:
 */
object StringUtils {

    fun urlToString(url:String):String{
        return url.replace("\\W+".toRegex(), "")
    }

}