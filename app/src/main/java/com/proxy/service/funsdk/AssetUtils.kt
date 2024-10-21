package com.proxy.service.funsdk

import android.content.Context
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.io.File
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2024/4/3 14:01
 * @desc:
 */
object AssetUtils {

    fun copyFolderFromAssets(
        context: Context,
        assetsFolderPath: String,
        destinationPath: String,
        callback: () -> Unit
    ) {
        val file = File(destinationPath)
        if (file.exists()) {
            callback()
            return
        }
        CsTask.ioThread()?.call(object :ICallable<String>{
            override fun accept(): String {
                try {
                    copyDir(context, assetsFolderPath, destinationPath)
                } catch (_: Throwable) {
                }
                return ""
            }
        })?.mainThread()?.doOnNext(object :IConsumer<String>{
            override fun accept(value: String) {
                callback()
            }
        })?.start()
    }

    private fun copyDir(context: Context, assetsFolderPath: String, destinationPath: String) {
        val assetManager = context.assets
        try {
            val files = assetManager.list(assetsFolderPath) ?: return
            val destination = File(destinationPath)
            if (!destination.exists()) destination.mkdirs()

            for (filename in files) {
                val assetFilePath = "$assetsFolderPath/$filename"
                val destFilePath = "${destination.path}/$filename"
                val subFiles = assetManager.list(assetFilePath)

                if (subFiles.isNullOrEmpty()) {
                    CsFileWriteUtils.setSourceAssetPath(assetFilePath).writeSync(destFilePath)
                } else {
                    copyDir(context, assetFilePath, destFilePath)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}