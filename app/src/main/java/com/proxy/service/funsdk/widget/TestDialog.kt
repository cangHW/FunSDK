package com.proxy.service.funsdk.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.widget.info.dialog.window.CsBaseDialog
import com.proxy.service.widget.info.dialog.window.info.DialogConfig
import com.proxy.service.widget.info.dialog.window.info.DialogGravity
import com.proxy.service.widget.info.dialog.window.info.PostConditionStrategy
import com.proxy.service.widget.info.dialog.window.info.PreConditionStrategy
import com.proxy.service.widget.info.dialog.window.info.ScreenOrientation
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/11/28 14:11
 * @desc:
 */
class TestDialog(private val index: Int) : CsBaseDialog() {

    override fun getDialogPreConditionStrategy(): PreConditionStrategy {
        return PreConditionStrategy.Default
    }

    override fun getDialogPostConditionStrategy(): PostConditionStrategy {
        return PostConditionStrategy.Default
    }

    override fun getDialogConfig(): DialogConfig {
        val config = DialogConfig()
//        config.gravity = DialogGravity.BOTTOM
//        config.focusable = true
//        config.screenOrientation = ScreenOrientation.ORIENTATION_LANDSCAPE
        config.width = CsDpUtils.dp2px(320f)
        config.height = CsDpUtils.dp2px(72f)
//        config.width = LayoutParams.MATCH_PARENT
//        config.height = LayoutParams.WRAP_CONTENT
        return config
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(context: Context, parent: ViewGroup): View? {
        super.onCreateView(context, parent)
        val view = LayoutInflater.from(context).inflate(R.layout.widget_dialog_test, parent, false)
        val content = view.findViewById<AppCompatTextView>(R.id.content)
        content.text = "$index"
        return view
    }

    private var disposable: ITaskDisposable? = null

    override fun onStart() {
        super.onStart()
        disposable = CsTask.delay(2, TimeUnit.SECONDS)?.doOnNext(object : IConsumer<Long> {
            override fun accept(value: Long) {
                dismiss()
            }
        })?.start()
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

}