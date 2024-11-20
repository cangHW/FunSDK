package com.proxy.service.permission.base.manager

import android.app.Activity
import androidx.annotation.StringDef
import com.proxy.service.permission.base.callback.ButtonClick

/**
 * 弹窗工厂
 *
 * @author: cangHX
 * @data: 2024/11/20 09:46
 * @desc:
 */
interface DialogFactory {

    companion object {
        const val MODE_RATIONALE = "rationale"
        const val MODE_SETTING = "setting"
    }

    @StringDef(value = [MODE_RATIONALE, MODE_SETTING])
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Mode

    /**
     * 展示弹窗
     *
     * @param mode              弹窗模式, [MODE_RATIONALE]、[MODE_SETTING]
     * @param activity          上下文环境
     * @param title             弹窗标题
     * @param content           弹窗内容
     * @param leftButtonText    左侧按钮文案
     * @param leftButtonClick   左侧按钮回调
     * @param rightButtonText   右侧按钮文案
     * @param rightButtonClick  右侧按钮回调
     * */
    fun showDialog(
        @Mode mode: String,
        activity: Activity,
        title: String?,
        content: String?,
        leftButtonText: String?,
        leftButtonClick: ButtonClick,
        rightButtonText: String?,
        rightButtonClick: ButtonClick
    )

}