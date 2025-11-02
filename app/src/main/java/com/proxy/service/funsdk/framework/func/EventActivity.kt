package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.message.broadcast.BroadcastMessageListener
import com.proxy.service.core.framework.app.message.broadcast.CsBroadcastManager
import com.proxy.service.core.framework.app.message.event.CsEventManager
import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.process.CsShareManager
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.woker.AbstractAsyncWorker
import com.proxy.service.core.framework.app.message.process.woker.AbstractSyncWorker
import com.proxy.service.core.framework.app.message.provider.CsProviderManager
import com.proxy.service.core.framework.app.message.provider.ProviderMessageListener
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkEventBinding
import java.lang.StringBuilder

/**
 * @author: cangHX
 * @data: 2024/10/29 11:37
 * @desc:
 */
class EventActivity : BaseActivity<ActivityFrameworkEventBinding>() {

    companion object {
        private const val BROADCAST_ACTION = "broadcast_action"
        private const val PROVIDER_METHOD = "test_message"
        private const val WORKER_SYNC_METHOD = "sync_method"
        private const val WORKER_ASYNC_METHOD = "async_method"

        fun launch(context: Context) {
            val intent = Intent(context, EventActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkEventBinding {
        return ActivityFrameworkEventBinding.inflate(inflater)
    }

    override fun initView() {
        binding?.checkBroadcast?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsBroadcastManager.addWeakReceiverListener(BROADCAST_ACTION, broadcastMessageListener)
            } else {
                CsBroadcastManager.removeReceiverListener(BROADCAST_ACTION, broadcastMessageListener)
            }
        }

        binding?.checkEvent?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsEventManager.addWeakCallback(mainThreadEventCallback, this)
                CsEventManager.addWeakCallback(workThreadEventCallback, this)
            } else {
                CsEventManager.removeCallback(mainThreadEventCallback)
                CsEventManager.removeCallback(workThreadEventCallback)
            }
        }

        binding?.checkContentProvider?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsProviderManager.addReceiverListener(PROVIDER_METHOD, providerMessageListener)
            } else {
                CsProviderManager.removeReceiverListener(PROVIDER_METHOD)
            }
        }

        binding?.checkProcess?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsShareManager.addWorker(syncWorker)
                CsShareManager.addWorker(asyncWorker)
            } else {
                CsShareManager.removeWorker(syncWorker)
                CsShareManager.removeWorker(asyncWorker)
            }
        }

        binding?.checkBroadcast?.isChecked = true
        binding?.checkEvent?.isChecked = true
        binding?.checkContentProvider?.isChecked = true
        binding?.checkProcess?.isChecked = true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.send_broadcast_msg -> {
                val bundle = Bundle()
                bundle.putString("ss", "sss")

                binding?.content?.addData("broadcast", "发送广播 bundle = $bundle")
                CsBroadcastManager.sendMessage(CsAppUtils.getPackageName(), null, bundle)
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

            R.id.send_content_provider_msg -> {
                val bundle = Bundle()
                bundle.putString("ss", "send")
                binding?.content?.addData("provider", "发送消息 bundle = $bundle")
                val result = CsProviderManager.sendMessage(
                    CsAppUtils.getPackageName(),
                    PROVIDER_METHOD,
                    "test-arg",
                    bundle
                )
                binding?.content?.addData("provider", "返回值 bundle = $result")
            }

            R.id.send_process_sync_msg -> {
                val pkg = "com.proxy.service.funsdk2"

                binding?.content?.addData("worker", "发送消息 $WORKER_SYNC_METHOD")
                val syncResult = CsShareManager.create(pkg, WORKER_SYNC_METHOD).execute()
                binding?.content?.addData("worker", "返回值 $syncResult")
            }

            R.id.send_process_async_msg -> {
                val pkg = "com.proxy.service.funsdk2"

                binding?.content?.addData("worker", "发送消息 $WORKER_ASYNC_METHOD")
                CsShareManager.create(pkg, WORKER_ASYNC_METHOD).enqueue(object : RequestCallback() {
                    override fun onFailed(code: Int, throwable: Throwable) {
                        binding?.content?.addData(
                            "worker",
                            "onFailed 返回值 code = $code $throwable"
                        )
                    }

                    override fun onProgress(
                        version: String,
                        time: Long,
                        method: String,
                        content: String
                    ) {
                        val builder = StringBuilder()
                        builder.append("version=").append(version).append(", ")
                        builder.append("time=").append(time).append(", ")
                        builder.append("method=").append(method).append(", ")
                        builder.append("content=").append(content)
                        binding?.content?.addData("worker", "onProgress 返回值 $builder")
                    }

                    override fun onSuccess(
                        version: String,
                        time: Long,
                        method: String,
                        content: String
                    ) {
                        val builder = StringBuilder()
                        builder.append("version=").append(version).append(", ")
                        builder.append("time=").append(time).append(", ")
                        builder.append("method=").append(method).append(", ")
                        builder.append("content=").append(content)
                        binding?.content?.addData("worker", "onSuccess 返回值 $builder")
                    }
                })
            }
        }
    }


    private val broadcastMessageListener = object : BroadcastMessageListener {
        override fun onReceive(context: Context, fromPkg: String, data: Uri?, extras: Bundle?) {
            binding?.content?.addData("broadcast", "接收广播 fromPkg = $fromPkg, data = $data, extras = $extras")
        }
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


    private val providerMessageListener = object : ProviderMessageListener {
        override fun onReceive(
            context: Context?,
            fromPkg: String,
            arg: String?,
            extras: Bundle?
        ): Bundle? {
            binding?.content?.addData(
                "provider",
                "接收消息 fromPkg = $fromPkg, arg = $arg, extras = $extras"
            )
            val bundle = Bundle()
            bundle.putString("qqq", "${System.currentTimeMillis()}")
            return bundle
        }
    }


    private val syncWorker = object : AbstractSyncWorker() {
        override fun doWork(request: ShareMessage): String {
            binding?.content?.addData("syncWorker", "接收消息 $request")
            return "abc"
        }

        override fun getMethodName(): String {
            return WORKER_SYNC_METHOD
        }
    }

    private val asyncWorker = object : AbstractAsyncWorker() {
        override fun doWork(request: ShareMessage, callback: WorkerProgressCallback) {
            binding?.content?.addData("asyncWorker", "接收消息 $request")
            callback.onProgress("111")
            callback.onProgress("222")
            callback.onProgress("333")
            callback.onFinish("end")
        }

        override fun getMethodName(): String {
            return WORKER_ASYNC_METHOD
        }
    }
}