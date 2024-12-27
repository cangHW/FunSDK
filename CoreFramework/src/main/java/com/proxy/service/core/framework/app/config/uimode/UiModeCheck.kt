package com.proxy.service.core.framework.app.config.uimode

import android.content.Context
import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.base.BaseConfigurationCheck
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2024/12/25 20:53
 * @desc:
 */
object UiModeCheck : BaseConfigurationCheck() {

    private var defaultMode: Int

    init {
        val uiMode = CsContextManager.getApplication().resources.configuration.uiMode
        defaultMode = uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

    override fun onSystemConfigurationChanged(newConfig: Configuration) {
        defaultMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

    override fun onConfigurationCheck(context: Context, config: Configuration): Boolean {
        val flag = UiModeStore.isFollowingSystem()
        val mode = if (flag) {
            defaultMode
        } else {
            UiModeStore.getValue() ?: defaultMode
        }

        val current = config.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (current == mode) {
            return false
        }

        config.uiMode = mode or (config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
        return true
    }
}