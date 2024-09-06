package com.proxy.service.apihttp.info.retrofit.converter.scalars

import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2024/6/19 11:20
 * @desc:
 */
object ScalarResponseBodyConverters {

    val stringResponseBodyConverter by lazy { StringResponseBodyConverter() }
    val booleanResponseBodyConverter by lazy { BooleanResponseBodyConverter() }
    val byteResponseBodyConverter by lazy { ByteResponseBodyConverter() }
    val charResponseBodyConverter by lazy { CharResponseBodyConverter() }
    val doubleResponseBodyConverter by lazy { DoubleResponseBodyConverter() }
    val floatResponseBodyConverter by lazy { FloatResponseBodyConverter() }
    val intResponseBodyConverter by lazy { IntResponseBodyConverter() }
    val longResponseBodyConverter by lazy { LongResponseBodyConverter() }
    val shortResponseBodyConverter by lazy { ShortResponseBodyConverter() }

    class StringResponseBodyConverter : Converter<ResponseBody, String> {
        override fun convert(value: ResponseBody): String {
            return value.string()
        }
    }

    class BooleanResponseBodyConverter : Converter<ResponseBody, Boolean> {
        override fun convert(value: ResponseBody): Boolean {
            return value.string().toBoolean()
        }
    }

    class ByteResponseBodyConverter : Converter<ResponseBody, Byte> {
        override fun convert(value: ResponseBody): Byte {
            return value.string().toByte()
        }
    }

    class CharResponseBodyConverter : Converter<ResponseBody, Char> {
        override fun convert(value: ResponseBody): Char {
            val body = value.string()
            if (body.length != 1) {
                throw IOException(
                    "Expected body of length 1 for Character conversion but was " + body.length
                )
            }
            return body[0]
        }
    }

    class DoubleResponseBodyConverter : Converter<ResponseBody, Double> {
        override fun convert(value: ResponseBody): Double {
            return value.string().toDouble()
        }
    }

    class FloatResponseBodyConverter : Converter<ResponseBody, Float> {
        override fun convert(value: ResponseBody): Float {
            return value.string().toFloat()
        }
    }

    class IntResponseBodyConverter : Converter<ResponseBody, Int> {
        override fun convert(value: ResponseBody): Int {
            return value.string().toInt()
        }
    }

    class LongResponseBodyConverter : Converter<ResponseBody, Long> {
        override fun convert(value: ResponseBody): Long {
            return value.string().toLong()
        }
    }

    class ShortResponseBodyConverter : Converter<ResponseBody, Short> {
        override fun convert(value: ResponseBody): Short {
            return value.string().toShort()
        }
    }

}


