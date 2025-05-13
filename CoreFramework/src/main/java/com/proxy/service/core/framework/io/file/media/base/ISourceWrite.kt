package com.proxy.service.core.framework.io.file.media.base

import java.io.File
import java.io.OutputStream

/**
 * @author: cangHX
 * @data: 2024/12/31 17:24
 * @desc:
 */
interface ISourceWrite {

    fun write(file: File): Boolean

    fun write(stream: OutputStream): Boolean

}