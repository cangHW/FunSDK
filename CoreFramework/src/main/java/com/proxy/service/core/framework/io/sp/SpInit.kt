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
    private val spMapper = HashMap<String, ISpController>()

    private fun init() {
        if (isInit.compareAndSet(false, true)) {
            rootPath = MMKV.initialize(CsContextManager.getApplication())
        }
    }

    fun getRootPath(): String {
        init()
        return rootPath
    }

    fun getSp(spName: String?, mode: SpMode, secretKey: String?): ISpController {
        init()

        val key = "CoreFw_${spName}"
        val value = spMapper[key]

        if (value != null) {
            return value
        }

        synchronized(spMapper) {
            val doubleCheck = spMapper[key]

            if (doubleCheck != null) {
                return doubleCheck
            }

            val mmkv = if (spName.isNullOrEmpty() || spName.isBlank()) {
                MMKV.defaultMMKV(mode.mode, secretKey)
            } else {
                MMKV.mmkvWithID(spName, mode.mode, secretKey)
            }
            val controller = SpControllerImpl(mmkv)
            spMapper[key] = controller
            return controller
        }
    }

    fun getAllSp(): List<ISpController> {
        synchronized(spMapper) {
            return spMapper.values.toList()
        }
    }

    fun remove(controller: ISpController) {
        synchronized(spMapper) {
            val iterator = spMapper.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.value == controller) {
                    iterator.remove()
                }
            }
        }
    }
}