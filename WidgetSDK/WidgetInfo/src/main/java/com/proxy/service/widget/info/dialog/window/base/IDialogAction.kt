package com.proxy.service.widget.info.dialog.window.base

import android.app.Activity
import com.proxy.service.widget.info.dialog.window.listener.OnDialogDismissListener

/**
 * @author: cangHX
 * @data: 2025/11/28 15:36
 * @desc:
 */
interface IDialogAction {

    /**
     * 设置弹窗关闭回调
     * */
    fun setOnDialogDismissListener(listener: OnDialogDismissListener)

    /**
     * 展示全局弹窗. 需要权限 [android.permission.SYSTEM_ALERT_WINDOW] 或 [android.settings.action.MANAGE_OVERLAY_PERMISSION]
     *
     * android6.0 以上需要引导用户开启权限, 开启方式：
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
    fun show()

    /**
     * 展示页面弹窗, 退出页面会自动关闭弹窗. 无需权限
     * */
    fun show(activity: Activity)

    /**
     * 关闭弹窗
     * */
    fun dismiss()
}