package com.proxy.service.core.framework.app.config.uimode

import com.proxy.service.core.framework.app.config.controller.store.BaseStore
import com.proxy.service.core.framework.data.log.CsLogger
import org.json.JSONObject

/**
 * @author: cangHX
 * @data: 2024/12/25 20:52
 * @desc:
 */
object UiModeStore : BaseStore<Int>() {

    private const val KEY_UI_MODE = "ui_mode"

    override fun saveToString(value: Int): String? {
        try {
            val json = JSONObject()
            json.put(KEY_UI_MODE, value)
            return json.toString()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    override fun loadFromString(value: String): Int? {
        try {
            val json = JSONObject(value)
            return json.getInt(KEY_UI_MODE)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }
}