package com.proxy.service.core.framework.app.install

import android.content.Intent

/**
 * @author: cangHX
 * @data: 2024/10/29 18:46
 * @desc:
 */
enum class InstallStatusEnum(val value: String) {

    /**
     * 安装应用
     */
    PACKAGE_ADDED(Intent.ACTION_PACKAGE_ADDED),

    /**
     * 删除应用
     */
    PACKAGE_REMOVED(Intent.ACTION_PACKAGE_REMOVED);

    companion object {
        fun of(action: String?): InstallStatusEnum? {
            if (action == PACKAGE_ADDED.value) {
                return PACKAGE_ADDED
            } else if (action == PACKAGE_REMOVED.value) {
                return PACKAGE_REMOVED
            }
            return null
        }
    }

}