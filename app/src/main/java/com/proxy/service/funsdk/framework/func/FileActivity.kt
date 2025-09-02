package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.read.CsFileReadUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkFileBinding
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/9/25 14:28
 * @desc:
 */
class FileActivity : BaseActivity<ActivityFrameworkFileBinding>() {

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

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkFileBinding {
        return ActivityFrameworkFileBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.write_file -> {
                val data = "测试数据 ${System.currentTimeMillis()} \n"
                CsFileWriteUtils.setSourceString(data).writeSync(filePath, true)
                binding?.content?.addData("写入数据", "写入成功 data = $data")
            }

            R.id.read_file -> {
                val content = CsFileReadUtils.setSourcePath(filePath).readString()

//                val content = CsFileReadUtils.setSourceFile(File(filePath)).read()

//                val stream = FileInputStream(File(filePath))
//                val content = CsFileReadUtils.setSourceStream(stream).read()

//                val reader = BufferedReader(FileReader(File(filePath)))
//                val content = CsFileReadUtils.setSourceReader(reader).readString()

                binding?.content?.addData("读取数据", "读取成功 data = $content")
            }

            R.id.file_merge -> {
                val srcPath = "$fileDir/merge/test1.txt"
                CsFileUtils.delete(srcPath)

                val srcData = "源文件:aaaaaaaa"
                CsFileWriteUtils.setSourceString(srcData).writeSync(srcPath)
                binding?.content?.addData("文件合并", "源文件内容 data = $srcData")

                val destPath = "$fileDir/merge/test2.txt"
                CsFileUtils.delete(destPath)

                val destData = "目标文件：bbbbbbb"
                CsFileWriteUtils.setSourceString(destData).writeSync(destPath)
                binding?.content?.addData("文件合并", "目标文件内容 data = $destData")

                CsFileWriteUtils.setSourcePath(srcPath).writeSync(destPath, true)
                val totalData = CsFileReadUtils.setSourcePath(destPath).readString()
                binding?.content?.addData("文件合并", "最终文件内容 data = $totalData")
            }

            R.id.save_album -> {
                binding?.content?.addData("相册", "准备存入相册")
                CsFileMediaUtils.getImageManager()
                    .setDisplayName("测试图片")
                    .setMimeType(MimeType.IMAGE_PNG)
                    .setSourceAssetPath("image/asd.png")
                    .insert(object : InsertCallback {
                        override fun onFailed() {
                            binding?.content?.addData("相册", "存入相册失败")
                        }

                        override fun onSuccess(path: String) {
                            binding?.content?.addData("相册", "存入相册成功. path = $path")
                        }
                    })
            }

            R.id.query_file -> {
                val mimeType = "image/*"
                binding?.content?.addData("查询", "准备查询设备中的文件, type = $mimeType")
                CsFileMediaUtils.getQueryManager()
                    .setMimeType(MimeType.create(mimeType))
                    .query(object : QueryCallback {
                        override fun onFailed() {
                            binding?.content?.addData("查询", "查询失败")
                        }

                        override fun onSuccess(list: ArrayList<DataInfo>) {
                            binding?.content?.addData("查询", "查询成功 \n $list")
                        }
                    })
            }
        }
    }

}