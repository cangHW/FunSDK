package com.proxy.service.core.framework.io.uri.info

import java.io.File

/**
 * @author: cangHX
 * @data: 2026/1/12 11:50
 * @desc:
 */
class SharePathInfo private constructor(
    private val name: String,
    private val path: String
) {

    companion object {
        fun create(name: String, path: String): SharePathInfo {
            return SharePathInfo(name, path)
        }
    }

    private var isShareFile = false

    init {
        isShareFile = !path.endsWith(File.separator)
    }

    /**
     * 是否匹配当前规则
     * */
    fun isMatchedWithEncode(filePath: String): Boolean {
        if (isShareFile) {
            return filePath == path
        }
        return filePath.startsWith(path)
    }

    /**
     * 路径转码
     * */
    fun encode(filePath: String): String {
        return filePath.replaceFirst(path, name)
    }

    /**
     * 是否匹配当前规则
     * */
    fun isMatchedWithDecode(filePath: String): Boolean {
        if (isShareFile) {
            return filePath == name
        }
        return filePath.startsWith(name)
    }

    /**
     * 路径转码
     * */
    fun decode(filePath: String): String {
        return filePath.replaceFirst(name, path)
    }

    override fun toString(): String {
        return "SharePathInfo(name='$name', path='$path', isShareFile=$isShareFile)"
    }

}