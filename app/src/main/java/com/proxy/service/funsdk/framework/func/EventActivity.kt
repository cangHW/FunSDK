package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.message.broadcast.CsBroadcastManager
import com.proxy.service.core.framework.app.message.broadcast.MessageReceiverListener
import com.proxy.service.core.framework.app.message.event.CsEventManager
import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/10/29 11:37
 * @desc:
 */
class EventActivity : AppCompatActivity(), MessageReceiverListener {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, EventActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.set_callback -> {
                CsBroadcastManager.addWeakReceiverListener(this)
                CsEventManager.addWeakCallback(mainThreadEventCallback, this)
                CsEventManager.addWeakCallback(workThreadEventCallback, this)
            }

            R.id.send_broadcast_msg -> {
                val bundle = Bundle()
                bundle.putString("ss", "sss")
                CsBroadcastManager.sendBroadcast(CsAppUtils.getPackageName(), null, bundle)
            }

            R.id.send_main_event_msg -> {
                for (index in 0..600) {
                    CsEventManager.sendEventValue(MainBean("$index"))
                }
            }

            R.id.send_thread_event_msg -> {
                for (index in 0..10) {
                    CsEventManager.sendEventValue(WorkBean("$index"))
                }
            }
        }
    }

    override fun onReceive(context: Context, data: Uri?, extras: Bundle?) {
        Toast.makeText(context, "Broadcast = $extras", Toast.LENGTH_SHORT).show()
    }

    private class MainBean(val tag:String){
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
            CsLogger.tag("event").d("onMainEvent, any = $any")
            Toast.makeText(this@EventActivity, "MainEvent", Toast.LENGTH_SHORT).show()
        }
    }

    private class WorkBean(val tag:String){
        override fun toString(): String {
            return "[tag = $tag]"
        }
    }

    private val workThreadEventCallback = object : WorkThreadEventCallback {
        override fun getSupportTypes(): Set<Class<*>> {
            return setOf(WorkBean::class.java)
        }

        override fun onWorkEvent(any: Any) {
            CsLogger.tag("event").d("onWorkEvent, any = $any")
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    Toast.makeText(this@EventActivity, "WorkEvent", Toast.LENGTH_SHORT).show()
                    return ""
                }
            })?.start()
        }
    }

}