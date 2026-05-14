package com.proxy.service.core.framework.app.message.process.whitelist

import android.content.Context
import android.content.pm.PackageManager
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.install.callback.InstallReceiverListener
import com.proxy.service.core.framework.app.install.status.InstallStatusEnum
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: cangHX
 * @date: 2026/5/13 20:17
 * @desc:
 */
sealed class WhitelistFactory {

    abstract fun isAllowedSender(context: Context, pkg: String): Boolean

    /**
     * 全部允许, 危险, 不建议使用
     * */
    object AllowEveryOne : WhitelistFactory() {
        override fun isAllowedSender(context: Context, pkg: String): Boolean {
            return true
        }
    }

    /**
     * 签名校验, 需要发送方与接收方使用相同签名
     * */
    object Signature : WhitelistFactory(), InstallReceiverListener {

        private val signatureMap = ConcurrentHashMap<String, String>()

        init {
            CsInstallUtils.addWeakReceiverListener(this)
        }

        override fun isAllowedSender(context: Context, pkg: String): Boolean {
            val currentPkg = CsAppUtils.getPackageName()
            val current = if (signatureMap.contains(currentPkg)) {
                signatureMap[currentPkg]
            } else {
                val hash = CsAppUtils.getSignatureHash()
                signatureMap[currentPkg] = hash ?: ""
                hash
            }
            if (current.isNullOrBlank()) {
                return false
            }

            val remote = if (signatureMap.contains(pkg)) {
                signatureMap[pkg]
            } else {
                val hash = CsAppUtils.getSignatureHash(pkg)
                signatureMap[pkg] = hash ?: ""
                hash
            }
            if (remote.isNullOrBlank()) {
                return false
            }

            return current == remote
        }

        override fun onReceive(
            context: Context,
            installStatusEnum: InstallStatusEnum,
            packageName: String
        ) {
            if (
                installStatusEnum == InstallStatusEnum.PACKAGE_REMOVED ||
                installStatusEnum == InstallStatusEnum.PACKAGE_REPLACED
            ) {
                signatureMap.remove(packageName)
            }
        }
    }

    /**
     * 权限校验, 需要发送方具有接收方的自定义权限 [${applicationId}.permission.BROADCAST_EVENT]
     * */
    object Permission : WhitelistFactory() {

        private const val PERMISSION_SUFFIX = ".permission.BROADCAST_EVENT"

        override fun isAllowedSender(context: Context, pkg: String): Boolean {
            val permission = "${CsAppUtils.getPackageName()}$PERMISSION_SUFFIX"
            return hasPermission(context, pkg, permission)
        }

        private fun hasPermission(context: Context, pkg: String, permission: String): Boolean {
            return context.packageManager.checkPermission(
                permission,
                pkg
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 自定义白名单策略
     * */
    abstract class Custom : WhitelistFactory()
}