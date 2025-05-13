package com.proxy.service.apihttp.base.download.config

import com.proxy.service.core.constants.CoreConfig

/**
 * @author: cangHX
 * @data: 2024/10/31 10:15
 * @desc:
 */
class DownloadGroup private constructor(val groupName: String, val priority: Int, val fileDir: String) {

    companion object {
        private const val TAG = "${CoreConfig.TAG}DownloadGroup"

        fun builder(groupName: String): Builder {
            return Builder(groupName)
        }
    }

    class Builder(private val groupName: String) {

        private var groupPriority: Int = 0
        private var groupFileDir: String? = null

        /**
         * 设置组优先级
         * */
        fun setPriority(priority: Int): Builder {
            this.groupPriority = priority
            return this
        }

        /**
         * 设置组默认路径，最终下载路径由任务配置路径、组配置路径、默认路径综合得出
         * */
        fun setDir(fileDir: String): Builder {
            if (fileDir.trim().isNotEmpty()) {
                this.groupFileDir = fileDir
            }
            return this
        }

        fun build(): DownloadGroup {
            return DownloadGroup(groupName, groupPriority, groupFileDir ?: "")
        }
    }

}