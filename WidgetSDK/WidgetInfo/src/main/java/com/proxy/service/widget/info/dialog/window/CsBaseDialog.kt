package com.proxy.service.widget.info.dialog.window

import android.app.Activity
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.dialog.window.base.AbstractLifecycleDialog
import com.proxy.service.widget.info.dialog.window.listener.OnDialogDismissListener
import com.proxy.service.widget.info.dialog.window.manager.DialogManager

/**
 * @author: cangHX
 * @data: 2025/11/27 10:55
 * @desc:
 */
abstract class CsBaseDialog : AbstractLifecycleDialog() {

    /**
     * 设置弹窗关闭回调
     * */
    override fun setOnDialogDismissListener(listener: OnDialogDismissListener) {
        this.dialogDismissListener = listener
    }

    /**
     * 展示全局弹窗.
     *
     * 需要权限
     * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
     *
     * android6.0 以上需要引导用户开启权限, 开启方式：
     *
     * val REQUEST_CODE_OVERLAY_PERMISSION = 100
     * val intent = Intent(
     *     Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
     *     Uri.parse("package:" + CsAppUtils.getPackageName())
     * )
     * startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
     *
     * .....................
     *
     * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     *     super.onActivityResult(requestCode, resultCode, data);
     *     if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
     *         if (Settings.canDrawOverlays(this)) {
     *             // 用户授予了权限
     *             Log.d("OverlayPermission", "Permission granted");
     *         } else {
     *             // 用户未授予权限
     *             Log.d("OverlayPermission", "Permission not granted");
     *         }
     *     }
     * }
     * */
    override fun show() {
        context = CsContextManager.getApplication()
        if (!Settings.canDrawOverlays(context)) {
            CsLogger.tag(tag).e("The dialog cannot be displayed because of the lack of permission.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        DialogManager.requestShow(this)
    }

    /**
     * 展示页面弹窗, 退出页面会自动关闭弹窗. 无需权限
     * */
    override fun show(activity: Activity) {
        context = activity
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        params.token = activity.window.decorView.windowToken
        DialogManager.requestShow(this)
    }

    /**
     * 关闭弹窗
     * */
    override fun dismiss() {
        DialogManager.requestDismiss(this)
    }

    /**
     * 是否已添加
     * */
    fun isAdded(): Boolean {
        return isCreate.get()
    }

    /**
     * 是否正在展示
     * */
    fun isShowing(): Boolean {
        return isCreateView.get()
    }
}