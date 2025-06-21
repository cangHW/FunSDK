package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.message.broadcast.CsBroadcastManager
import com.proxy.service.core.framework.app.message.broadcast.MessageReceiverListener
import com.proxy.service.core.framework.app.message.event.CsEventManager
import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkEventBinding

/**
 * @author: cangHX
 * @data: 2024/10/29 11:37
 * @desc:
 */
class EventActivity : BaseActivity<ActivityFrameworkEventBinding>(), MessageReceiverListener {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, EventActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        binding?.checkBroadcast?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsBroadcastManager.addWeakReceiverListener(this)
            } else {
                CsBroadcastManager.removeReceiverListener(this)
            }
        }

        binding?.checkEvent?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsEventManager.addWeakCallback(mainThreadEventCallback, this)
                CsEventManager.addWeakCallback(workThreadEventCallback, this)
            } else {
                CsEventManager.remove(mainThreadEventCallback)
                CsEventManager.remove(workThreadEventCallback)
            }
        }

        binding?.checkBroadcast?.isChecked = true
        binding?.checkEvent?.isChecked = true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.send_broadcast_msg -> {
                val bundle = Bundle()
                bundle.putString("ss", "sss")

                binding?.content?.addData("broadcast", "发送广播 bundle = $bundle")
                CsBroadcastManager.sendBroadcast(CsAppUtils.getPackageName(), null, bundle)
            }

            R.id.send_main_event_msg -> {
                binding?.content?.addData("event", "发送 main event")

                for (index in 0..600) {
                    CsEventManager.sendEventValue(MainBean("$index"))
                }
            }

            R.id.send_thread_event_msg -> {
                binding?.content?.addData("event", "发送 work event")

                for (index in 0..10) {
                    CsEventManager.sendEventValue(WorkBean("$index"))
                }
            }
        }
    }

    override fun onReceive(context: Context, data: Uri?, extras: Bundle?) {
        binding?.content?.addData("broadcast", "接收广播 data = $data, extras = $extras")
    }

    private class MainBean(val tag: String) {
        override fun toString(): String {
            return "[tag = $tag]"
        }
    }

    private val mainThreadEventCallback = object : MainThreadEventCallback {

        override fun shouldReceiveOnlyLatestMessage(): Boolean {
            return true
        }

        override fun getSupportTypes(): Set<Class<*>> {
            return setOf(MainBean::class.java)
        }

        override fun onMainEvent(any: Any) {
            binding?.content?.addData("event", "接收 MainEvent, any = $any")
        }
    }

    private class WorkBean(val tag: String) {
        override fun toString(): String {
            return "[tag = $tag]"
        }
    }

    private val workThreadEventCallback = object : WorkThreadEventCallback {
        override fun getSupportTypes(): Set<Class<*>> {
            return setOf(WorkBean::class.java)
        }

        override fun onWorkEvent(any: Any) {
            binding?.content?.addData("event", "接收 WorkEvent, any = $any")
        }
    }

}