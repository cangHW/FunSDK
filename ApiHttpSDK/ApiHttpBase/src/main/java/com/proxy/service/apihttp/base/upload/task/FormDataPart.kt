package com.proxy.service.apihttp.base.upload.task

import java.io.File

/**
 * @author: cangHX
 * @data: 2024/12/17 20:29
 * @desc:
 */
class FormDataPart private constructor(
    val type: String,
    val name: String,
    val value: String?,
    val fileName: String?,
    val file: File?
) {

    companion object {

        const val TYPE_TXT = "type_txt"
        const val TYPE_STREAM = "type_stream"

        fun create(name: String, value: String): FormDataPart {
            return FormDataPart(TYPE_TXT, name, value, null, null)
        }

        fun create(name: String, fileName: String, file: File): FormDataPart {
            return FormDataPart(TYPE_STREAM, name, null, fileName, file)
        }

    }

}