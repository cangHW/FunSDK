package com.proxy.service.funsdk.document.image

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.document.image.crop.ImageCropActivity
import com.proxy.service.funsdk.document.image.preview.ImagePreviewActivity

/**
 * @author: cangHX
 * @data: 2025/5/30 18:58
 * @desc:
 */
class ImageActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ImageActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_image)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.preview -> {
                ImagePreviewActivity.launch(this)
            }

            R.id.crop -> {
                ImageCropActivity.launch(this)
            }
        }
    }
}