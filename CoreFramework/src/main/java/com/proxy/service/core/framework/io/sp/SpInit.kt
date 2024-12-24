package com.proxy.service.core.framework.io.sp

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.tencent.mmkv.MMKV
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/7/20 14:32
 * @desc:
 */
object SpInit {

    const val TAG = "${CoreConfig.TAG}Sp"

    private val isInit = AtomicBoolean(false)

    private var rootPath: String = ""
    private val SpMapper = HashMap<String, MMKV>()

    private fun init() {
        if (isInit.compareAndSet(false, true)) {
            rootPath = MMKV.initialize(CsContextManager.getApplication())
        }
    }

    fun getRootPath(): String {
        init()
        return rootPath
    }

    fun getSp(spName: String?, mode: SpMode, secretKey: String?): MMKV {
        init()

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