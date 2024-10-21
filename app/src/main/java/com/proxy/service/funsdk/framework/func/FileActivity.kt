package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.read.CsFileReadUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.funsdk.R
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

    private var fileDir  = "/storage/emulated/0/Android/data/${CsAppUtils.getPackageName()}/files"
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
        }
    }

}