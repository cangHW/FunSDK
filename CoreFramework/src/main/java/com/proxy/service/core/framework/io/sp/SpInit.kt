package com.proxy.service.core.framework.io.sp

import android.content.Context
import com.proxy.service.core.constants.Constants
import com.tencent.mmkv.MMKV

/**
 * @author: cangHX
 * @data: 2024/7/20 14:32
 * @desc:
 */
object SpInit {

    const val TAG = "${Constants.TAG}Sp"

    var rootPath: String = ""
    private val SpMapper = HashMap<String, MMKV>()

    fun init(context: Context) {
        rootPath = MMKV.initialize(context)
    }

    fun getSp(spName: String?, mode: SpMode, secretKey: String?): MMKV {
        val key = "name_${spName}_mode_${mode.mode}_secretKey_${secretKey}"
        val value = SpMapper[key]

        if (value != null) {
            return value
        }

        synchronized(SpMapper) {
            val doubleCheck = SpMapper[key]

            if (doubleCheck != null) {
                return doubleCheck
            }

            val sp = if (spName.isNullOrEmpty() || spName.isBlank()) {
                MMKV.defaultMMKV(mode.mode, secretKey)
            } else {
                MMKV.mmkvWithID(spName, mode.mode, secretKey)
            }
            SpMapper[key] = sp
            return sp
        }
    }

    fun getAllSp(): List<MMKV> {
        synchronized(SpMapper) {
            return SpMapper.values.toList()
        }
    }

    fun remove(sp: MMKV) {
        synchronized(SpMapper) {
            SpMapper.entries.removeIf { it.value == sp }
        }
    }
}