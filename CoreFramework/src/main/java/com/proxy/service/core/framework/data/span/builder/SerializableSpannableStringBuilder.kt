package com.proxy.service.core.framework.data.span.builder

import android.text.SpannableStringBuilder
import java.io.Serializable

/**
 * @author: cangHX
 * @data: 2025/7/14 10:17
 * @desc:
 */
class SerializableSpannableStringBuilder : SpannableStringBuilder(), Serializable {

    companion object {
        private const val serialVersionUID: Long = 4909567650765875771L
    }

}