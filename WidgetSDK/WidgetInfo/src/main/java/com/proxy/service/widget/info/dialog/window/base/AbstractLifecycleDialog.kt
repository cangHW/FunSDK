package com.proxy.service.widget.info.dialog.window.base

import android.content.Context
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.widget.info.dialog.window.manager.WManager
import com.proxy.service.widget.info.dialog.window.view.DialogRootView
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2025/11/27 16:48
 * @desc:
 */
abstract class AbstractLifecycleDialog : AbstractActionDialog(), ILifecycleDialog {

    protected val isCreate = AtomicBoolean(false)
    protected val isCreateView = AtomicBoolean(false)
    private val isStart = AtomicBoolean(false)


    final override fun create() {
        if (isCreate.compareAndSet(false, true)) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    onCreate(context ?: CsContextManager.getApplication())
                    return ""
                }
            })?.start()
        }
    }

    final override fun start() {
        if (isCreateView.compareAndSet(false, true)) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    val ctx = context ?: return ""

                    val dialogRootView = DialogRootView(ctx)
                    CsLogger.tag(tag).i("onCreateView")
                    val view = onCreateView(ctx, dialogRootView)
                    if (view == null) {
                        rootView = null
                        return ""
                    }
                    rootContentView = view

                    if (view.parent == null && view != dialogRootView) {
                        dialogRootView.addView(view)
                    }
                    rootView = dialogRootView

                    dialogRootView.setOnRootViewTouchEventCallback(object :
                        DialogRootView.OnRootViewTouchEventCallback {
                        override fun dispatchTouchEvent(ev: MotionEvent?) {
                            onTouchEvent(ev)
                        }
                    })

                    dialogRootView.setOnRootViewKeyEventCallback(object :
                        DialogRootView.OnRootViewKeyEventCallback {
                        override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                            return onKeyDown(event)
                        }
                    })

                    setConfigToParams(getDialogConfig())
                    WManager.addView(dialogRootView, params)
                    return ""
                }
            })?.start()
        }

        if (isStart.compareAndSet(false, true)) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    onStart()
                    return ""
                }
            })?.start()
        }
    }

    final override fun stop() {
        if (isStart.compareAndSet(true, false)) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    onStop()
                    return ""
                }
            })?.start()
        }
    }

    final override fun destroy() {
        if (isCreateView.compareAndSet(true, false)) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    onDestroyView()

                    rootView?.let {
                        WManager.removeView(it, params)
                    }
                    return ""
                }
            })?.start()
        }

        if (isCreate.compareAndSet(true, false)) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    onDestroy()

                    dialogDismissListener?.onDialogDismiss()
                    return ""
                }
            })?.start()
        }
    }


    @CallSuper
    override fun onCreate(context: Context) {
        CsLogger.tag(tag).i("onCreate")
    }

    @CallSuper
    override fun onStart() {
        CsLogger.tag(tag).i("onStart")
    }

    @CallSuper
    override fun onStop() {
        CsLogger.tag(tag).i("onStop")
    }

    @CallSuper
    override fun onDestroyView() {
        CsLogger.tag(tag).i("onDestroyView")
    }

    @CallSuper
    override fun onDestroy() {
        CsLogger.tag(tag).i("onDestroy")
    }

}