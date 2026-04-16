package com.proxy.service.camera.base.loader.info

import android.util.Size
import com.proxy.service.core.framework.data.json.CsJsonUtils
import org.json.JSONObject

/**
 * @author: cangHX
 * @data: 2026/4/15 20:30
 * @desc:
 */
class SupportSize private constructor(
    val type: Int,
    val ratio: Float,
    val width: Int,
    val height: Int
) {

    companion object {
        private const val KEY_TYPE = "type"
        private const val KEY_RATIO = "ratio"
        private const val KEY_WIDTH = "width"
        private const val KEY_HEIGHT = "height"

        const val RATIO_1_1 = 1f
        const val RATIO_4_3 = 1.33333f
        const val RATIO_16_9 = 1.77777f

        const val TYPE_SIZE_16_9 = 3
        const val TYPE_SIZE_4_3 = 2
        const val TYPE_SIZE_1_1 = 1
        const val TYPE_SIZE_UNKNOWN = 0

        fun create(width: Int, height: Int): SupportSize {
            if (width == height) {
                return SupportSize(TYPE_SIZE_1_1, RATIO_1_1, width, height)
            }

            if (width / 4 == height / 3) {
                return SupportSize(TYPE_SIZE_4_3, RATIO_4_3, width, height)
            }

            if (width / 16 == height / 9) {
                return SupportSize(TYPE_SIZE_16_9, RATIO_16_9, width, height)
            }
            return SupportSize(TYPE_SIZE_UNKNOWN, width.toFloat() / height, width, height)
        }

        fun fromJson(json: String?): SupportSize? {
            if (json == null) {
                return null
            }
            try {
                val obj = JSONObject(json)
                val type = obj.getInt(KEY_TYPE)
                val ratio = obj.getString(KEY_RATIO).toFloat()
                val width = obj.getInt(KEY_WIDTH)
                val height = obj.getInt(KEY_HEIGHT)
                return SupportSize(type, ratio, width, height)
            } catch (_: Throwable) {
            }
            return CsJsonUtils.fromJson(json, SupportSize::class.java)
        }
    }

    fun toSize(): Size {
        return Size(width, height)
    }

    fun toJson(): String {
        val json = JSONObject()
        json.put(KEY_TYPE, type)
        json.put(KEY_RATIO, ratio.toString())
        json.put(KEY_WIDTH, width)
        json.put(KEY_HEIGHT, height)
        return json.toString()
    }

    override fun toString(): String {
        val name = when (type) {
            TYPE_SIZE_1_1 -> {
                "[1:1]"
            }

            TYPE_SIZE_4_3 -> {
                "[4:3]"
            }

            TYPE_SIZE_16_9 -> {
                "[16:9]"
            }

            else -> {
                ""
            }
        }
        return "{type=$name, ratio=$ratio, width=$width, height=$height}"
    }

}