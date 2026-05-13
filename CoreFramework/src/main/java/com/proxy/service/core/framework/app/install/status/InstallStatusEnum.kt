package com.proxy.service.core.framework.app.install.status

import android.content.Intent

/**
 * @author: cangHX
 * @date: 2024/10/29 18:46
 * @desc:
 */
enum class InstallStatusEnum(val value: String) {

    /**
     * 安装应用
     */
    PACKAGE_ADDED(Intent.ACTION_PACKAGE_ADDED),

    /**
     * 更新应用
     * */
    PACKAGE_REPLACED(Intent.ACTION_PACKAGE_REPLACED),

    /**
     * 删除应用
     */
    PACKAGE_REMOVED(Intent.ACTION_PACKAGE_REMOVED);

    companion object {
        fun of(action: String?): InstallStatusEnum? {
            if (action == PACKAGE_ADDED.value) {
                return PACKAGE_ADDED
            } else if (action == PACKAGE_REPLACED.value) {
                return PACKAGE_REPLACED
            } else if (action == PACKAGE_REMOVED.value) {
                return PACKAGE_REMOVED
            }
            return null
        }
    }

}