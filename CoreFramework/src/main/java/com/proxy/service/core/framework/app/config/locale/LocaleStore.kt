package com.proxy.service.core.framework.app.config.locale

import com.proxy.service.core.framework.app.config.controller.store.BaseStore
import com.proxy.service.core.framework.data.log.CsLogger
import org.json.JSONObject
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2024/12/25 11:09
 * @desc:
 */
object LocaleStore : BaseStore<Locale>() {

    private const val KEY_LANGUAGE = "language"
    private const val KEY_COUNTRY = "country"
    private const val KEY_VARIANT = "variant"

    override fun saveToString(value: Locale): String? {
        try {
            val language = value.language
            val country = value.country
            val variant = value.variant
            val json = JSONObject()
            json.put(KEY_LANGUAGE, language)
            json.put(KEY_COUNTRY, country)
            json.put(KEY_VARIANT, variant)
            return json.toString()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    override fun loadFromString(value: String): Locale? {
        try {
            val json = JSONObject(value)
            val language = json.getString(KEY_LANGUAGE)
            val country = json.getString(KEY_COUNTRY)
            val variant = json.getString(KEY_VARIANT)
            return Locale(language, country, variant)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }
}