package com.proxy.service.permission.info.utils

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger


/**
 * @author: cangHX
 * @data: 2024/11/21 10:08
 * @desc:
 */
object PermissionUtils {

    fun isPermissionGranted(permission: String): Boolean{
        val context = CsContextManager.getApplication()
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionDeclaredInManifest(permission: String): Boolean {
        try {
            val packageInfo = CsContextManager.getApplication().packageManager.getPackageInfo(
                CsAppUtils.getPackageName(),
                PackageManager.GET_PERMISSIONS
            )

            if (packageInfo.requestedPermissions != null) {
                for (p in packageInfo.requestedPermissions) {
                    if (p == permission) {
                        return true
                    }
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return false
    }

}