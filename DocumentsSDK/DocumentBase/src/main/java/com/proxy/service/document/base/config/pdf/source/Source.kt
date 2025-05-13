package com.proxy.service.document.base.config.pdf.source

import java.io.File

/**
 * @author: cangHX
 * @data: 2025/5/2 11:17
 * @desc:
 */
abstract class BaseSource(val password: String?)

class AssetPathSource(val assetPath: String, password: String?) : BaseSource(password) {
    override fun toString(): String {
        return "AssetPathSource(assetPath='$assetPath', password='$password')"
    }
}

class FilePathSource(val filePath: String, password: String?) : BaseSource(password) {
    override fun toString(): String {
        return "FilePathSource(filePath='$filePath', password='$password')"
    }
}

class FileSource(val file: File, password: String?) : BaseSource(password) {
    override fun toString(): String {
        return "FileSource(file='${file.name}', password='$password')"
    }
}

class ByteArraySource(val byteArray: ByteArray, password: String?) : BaseSource(password) {
    override fun toString(): String {
        return "ByteArraySource(byteArray='${byteArray.size}', password='$password')"
    }
}