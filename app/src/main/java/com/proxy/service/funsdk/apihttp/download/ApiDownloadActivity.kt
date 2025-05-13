package com.proxy.service.funsdk.apihttp.download

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.apihttp.CsApiDownload
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/11/5 13:30
 * @desc:
 */
class ApiDownloadActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ApiDownloadActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val test_url_BaiduNetdisk = "https://ad986c-1905179982.antpcdn.com:19001/b/pkg-ant.baidu.com/issue/netdisk/MACguanjia/4.38.0/BaiduNetdisk_mac_4.38.0_x64.dmg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_download)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.download_init -> {
                CsApiDownload.init(
                    DownloadConfig.builder()
                        .setMaxTask(2)
                        .addGroup(
                            DownloadGroup.builder("test").build()
                        )
                        .setAutoRestartOnNetworkReconnect(true)
                        .build()
                )
            }

            R.id.set_global_callback ->{
                CsApiDownload.registerGlobalDownloadCallback(globalCallback)
            }

            R.id.remove_global_callback ->{
                CsApiDownload.unregisterGlobalDownloadCallback(globalCallback)
            }

            R.id.download_start_normal -> {
                CsApiDownload.addTask(
                    DownloadTask.builder(test_url_BaiduNetdisk)
                        .setGroupName("test")
                        .setFileName("111.file")
                        .setTaskTag("111")
                        .setFileSize(338693085)
                        .setFileMd5("e8f12b89d8f03e461e8886fdeb69f2b8")
                        .build(),
                    callback
                )
            }

            R.id.download_start_part -> {
                CsApiDownload.addTask(
                    DownloadTask.builder(test_url_BaiduNetdisk)
                        .setFileName("222.file")
                        .setTaskTag("222")
                        .setFileSize(338693085)
                        .setMultiPartEnable(true)
                        .setFileMd5("e8f12b89d8f03e461e8886fdeb69f2b8")
                        .build(),
                    callback
                )
            }

            R.id.download_restart -> {
                CsApiDownload.reStartTask("222")
            }

            R.id.download_cancel_group -> {
                CsApiDownload.cancelGroup("test")
            }

            R.id.download_cancel_all -> {
                CsApiDownload.cancelAllTask()
            }

            R.id.download_cancel -> {
                CsApiDownload.cancel("111")
//                CsApiDownload.reStartTask("111")
                CsApiDownload.cancel("222")
            }

            R.id.get_download_type -> {
                CsLogger.d("任务 111 下载状态 = ${CsApiDownload.getDownloadStatus("111").status}")
                CsLogger.d("任务 222 下载状态 = ${CsApiDownload.getDownloadStatus("222").status}")
            }

            R.id.download_reset -> {
                CsApiDownload.resetRunningTask()
            }
        }
    }

    private val callback = object : DownloadCallback {
        override fun onWaiting(task: DownloadTask) {
            CsLogger.d("onWaiting, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onStart(task: DownloadTask) {
            CsLogger.d("onStart, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onProgress(
            task: DownloadTask,
            currentSize: Long,
            progress: Float,
            speed: Long
        ) {
            CsLogger.d("onProgress, currentSize = $currentSize, progress = $progress, speed = $speed, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onSuccess(task: DownloadTask) {
            CsLogger.d("onSuccess, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onCancel(task: DownloadTask) {
            CsLogger.d("onCancel, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onFailed(task: DownloadTask, exception: DownloadException) {
            CsLogger.d("onFailed, DownloadTask = ${task.getTaskTag()}, throwable = $exception")
        }
    }

    private val globalCallback = object :DownloadCallback{
        override fun onWaiting(task: DownloadTask) {
            CsLogger.d("global onWaiting, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onStart(task: DownloadTask) {
            CsLogger.d("global onStart, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onProgress(
            task: DownloadTask,
            currentSize: Long,
            progress: Float,
            speed: Long
        ) {
            CsLogger.d("global onProgress, currentSize = $currentSize, progress = $progress, speed = $speed, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onSuccess(task: DownloadTask) {
            CsLogger.d("global onSuccess, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onCancel(task: DownloadTask) {
            CsLogger.d("global onCancel, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onFailed(task: DownloadTask, exception: DownloadException) {
            CsLogger.d("global onFailed, DownloadTask = ${task.getTaskTag()}, throwable = $exception")
        }
    }
}