package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.read.CsFileReadUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * @author: cangHX
 * @data: 2024/9/25 14:28
 * @desc:
 */
class FileActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, FileActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var fileDir = "/storage/emulated/0/Android/data/${CsAppUtils.getPackageName()}/files"
    private var filePath = "$fileDir/text/test.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_file)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.write_file -> {
                CsFileWriteUtils.setSourceString("测试数据 ${System.currentTimeMillis()} \n")
                    .writeSync(filePath, true)
            }

            R.id.read_file -> {
//                val content = CsFileReadUtils.setSourcePath(filePath).read()

//                val content = CsFileReadUtils.setSourceFile(File(filePath)).read()

//                val stream = FileInputStream(File(filePath))
//                val content = CsFileReadUtils.setSourceStream(stream).read()

                val reader = BufferedReader(FileReader(File(filePath)))
                val content = CsFileReadUtils.setSourceReader(reader).readString()

                CsLogger.d(content)
            }

            R.id.file_merge -> {
                val srcPath = "$fileDir/merge/test1.txt"
                CsFileUtils.delete(srcPath)
                CsFileWriteUtils.setSourceString("源文件内容").writeSync(srcPath)
                CsLogger.d(CsFileReadUtils.setSourcePath(srcPath).readString())

                val destPath = "$fileDir/merge/test2.txt"
                CsFileUtils.delete(destPath)
                CsFileWriteUtils.setSourceString("目标文件内容").writeSync(destPath)
                CsLogger.d(CsFileReadUtils.setSourcePath(destPath).readString())

                CsFileWriteUtils.setSourcePath(srcPath).writeSync(destPath, true)
                CsLogger.d(CsFileReadUtils.setSourcePath(destPath).readString())
            }

            R.id.save_album -> {
                CsFileMediaUtils.getImageManager()
                    .setDisplayName("测试图片")
                    .setMimeType(MimeType.IMAGE_PNG)
                    .setSourceAssetPath("image/asd.png")
                    .insert(object : InsertCallback {
                        override fun onFailed() {
                            CsLogger.i("insert onFailed")
                        }

                        override fun onSuccess(path: String) {
                            CsLogger.i("insert onSuccess path = $path")
                        }
                    })
            }

            R.id.query_file -> {
                CsFileMediaUtils.getQueryManager()
                    .setMimeType(MimeType.create("*/*"))
//                    .setMimeType(MimeType.IMAGE_JPEG)
                    .query(object : QueryCallback {
                        override fun onFailed() {

                        }

                        override fun onSuccess(list: ArrayList<DataInfo>) {
                            list.forEach { info ->
                                CsTask.ioThread()?.call(object : ICallable<String> {
                                    override fun accept(): String {
                                        CsFileWriteUtils.setSourcePath(info.path)
                                            .writeAsync(
                                                Environment.getExternalStoragePublicDirectory(
                                                    Environment.DIRECTORY_DOWNLOADS
                                                ).absolutePath + File.separator + "asd.jpg"
                                            )
                                        return ""
                                    }
                                })?.start()
                            }
                        }
                    })
            }
        }
    }

}