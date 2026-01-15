package com.proxy.service.core.framework.io.uri.info

import java.io.File

/**
 * @author: cangHX
 * @data: 2026/1/12 11:50
 * @desc:
 */
class SharePathInfo private constructor(
    private val isShareFile: Boolean,
    val name: String,
    val path: String
) {

    companion object {
        fun create(name: String, path: String): SharePathInfo {
            val isShareFile = !path.endsWith(File.separator)
            var realName = name
            if (!realName.startsWith(File.separator)) {
                realName = "${File.separator}$realName"
            }
            if (realName.endsWith(File.separator)) {
                realName = realName.substring(0, realName.length - 1)
            }
            var realPath = path
            if (realPath.endsWith(File.separator)) {
                realPath = realPath.substring(0, realPath.length - 1)
            }
            return SharePathInfo(isShareFile, realName, realPath)
        }
    }

    private fun isMatched(filePath: String, target: String): Boolean {
        if (isShareFile) {
            return filePath == target
        }
        return filePath.startsWith(target)
    }

    /**
     * 是否匹配当前规则
     * */
    fun isMatchedWithEncode(filePath: String): Boolean {
        return isMatched(filePath, path)
    }

    /**
     * 路径转码
     * */
    fun encode(filePath: String): String {
        if (isShareFile) {
            return name
        }
        return filePath.replaceFirst(path, name)
    }

    /**
     * 是否匹配当前规则
     * */
    fun isMatchedWithDecode(filePath: String): Boolean {
        return isMatched(filePath, name)
    }

    /**
     * 路径转码
     * */
    fun decode(filePath: String): String {
        if (isShareFile) {
            return path
        }
        return filePath.replaceFirst(name, path)
    }

    /**
     * 是否完全相同
     * */
    fun isSame(info: SharePathInfo): Boolean {
        val isNameSame = name == info.name
        val isPathSame = path == info.path
        return isNameSame && isPathSame
    }

    override fun toString(): String {
        return "SharePathInfo(isShareFile=$isShareFile, name='$name', path='$path')"
    }
}