package com.proxy.service.funsdk.apihttp.download

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.core.service.apihttp.CsApiDownload
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityApiDownloadBinding

/**
 * @author: cangHX
 * @data: 2024/11/5 13:30
 * @desc:
 */
class ApiDownloadActivity : BaseActivity<ActivityApiDownloadBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ApiDownloadActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val test_url_BaiduNetdisk =
        "https://ad986c-1905179982.antpcdn.com:19001/b/pkg-ant.baidu.com/issue/netdisk/MACguanjia/4.38.0/BaiduNetdisk_mac_4.38.0_x64.dmg"

    override fun getViewBinding(inflater: LayoutInflater): ActivityApiDownloadBinding {
        return ActivityApiDownloadBinding.inflate(inflater)
    }

    override fun initView() {
        binding?.globalCallback?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsApiDownload.registerGlobalDownloadCallback(globalCallback)
            } else {
                CsApiDownload.unregisterGlobalDownloadCallback(globalCallback)
            }
        }
    }

    override fun onClick(view: View) {
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
                binding?.content?.addData("初始化", "下载初始化")
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
                binding?.content?.addData("Task", "添加一个普通下载任务")
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
                binding?.content?.addData("Task", "添加一个分片下载任务")
            }

            R.id.download_restart -> {
                CsApiDownload.reStartTask("222")
                binding?.content?.addData("Task", "重新开始一个下载任务")
            }

            R.id.download_cancel_group -> {
                CsApiDownload.cancelGroup("test")
                binding?.content?.addData("Task", "取消一个下载任务组")
            }

            R.id.download_cancel_all -> {
                CsApiDownload.cancelAllTask()
                binding?.content?.addData("Task", "取消全部下载任务")
            }

            R.id.download_cancel -> {
                CsApiDownload.cancel("111")
//                CsApiDownload.reStartTask("111")
                CsApiDownload.cancel("222")
                binding?.content?.addData("Task", "取消下载任务")
            }

            R.id.get_download_type -> {
                val builder = StringBuilder("\n")
                builder.append("任务 111 下载状态 = ")
                    .append(CsApiDownload.getDownloadStatus("111").status)
                    .append("\n")
                builder.append("任务 222 下载状态 = ")
                    .append(CsApiDownload.getDownloadStatus("222").status)
                    .append("\n")
                binding?.content?.addData("Task", "下载任务状态. $builder")
            }

            R.id.download_reset -> {
                CsApiDownload.resetRunningTask()
                binding?.content?.addData("Task", "刷新下载任务, 基于优先级执行任务")
            }
        }
    }

    private val callback = object : DownloadCallback {
        override fun onWaiting(task: DownloadTask) {
            binding?.content?.addData("DownloadCallback", "onWaiting, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onStart(task: DownloadTask) {
            binding?.content?.addData("DownloadCallback", "onStart, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onProgress(
            task: DownloadTask,
            currentSize: Long,
            progress: Float,
            speed: Long
        ) {
            binding?.content?.addData("DownloadCallback", "onProgress, currentSize = $currentSize, progress = $progress, speed = $speed, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onSuccess(task: DownloadTask) {
            binding?.content?.addData("DownloadCallback", "onSuccess, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onCancel(task: DownloadTask) {
            binding?.content?.addData("DownloadCallback", "onCancel, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onFailed(task: DownloadTask, exception: DownloadException) {
            binding?.content?.addData("DownloadCallback", "onFailed, DownloadTask = ${task.getTaskTag()}, throwable = $exception")
        }
    }

    private val globalCallback = object : DownloadCallback {
        override fun onWaiting(task: DownloadTask) {
            binding?.content?.addData("GlobalDownloadCallback", "onWaiting, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onStart(task: DownloadTask) {
            binding?.content?.addData("GlobalDownloadCallback", "onStart, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onProgress(
            task: DownloadTask,
            currentSize: Long,
            progress: Float,
            speed: Long
        ) {
            binding?.content?.addData("GlobalDownloadCallback", "onProgress, currentSize = $currentSize, progress = $progress, speed = $speed, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onSuccess(task: DownloadTask) {
            binding?.content?.addData("GlobalDownloadCallback", "onSuccess, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onCancel(task: DownloadTask) {
            binding?.content?.addData("GlobalDownloadCallback", "onCancel, DownloadTask = ${task.getTaskTag()}")
        }

        override fun onFailed(task: DownloadTask, exception: DownloadException) {
            binding?.content?.addData("GlobalDownloadCallback", "onFailed, DownloadTask = ${task.getTaskTag()}, throwable = $exception")
        }
    }
}