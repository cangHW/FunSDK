package com.proxy.service.funsdk.document.image.preview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.service.document.CsDocumentImage
import com.proxy.service.document.base.image.callback.base.OnDoubleClickCallback
import com.proxy.service.document.base.image.loader.base.IController
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.databinding.ActivityDocumentImagePreviewBinding

/**
 * @author: cangHX
 * @data: 2025/6/4 20:03
 * @desc:
 */
class ImagePreviewActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var binding: ActivityDocumentImagePreviewBinding? = null

    private var controller: IController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentImagePreviewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        binding?.layout?.let {
            controller = CsDocumentImage.createPreviewLoader(this)
                ?.loadRes(R.drawable.crop)
                ?.setDoubleClickCallback(doubleClickCallback)
                ?.into(it)
        }
    }

    private val doubleClickCallback = object : OnDoubleClickCallback {
        override fun onDoubleClick(event: MotionEvent) {
            controller?.let {
                if (it.getCurrentScale() >= 2) {
                    it.setScale(1f, event.x, event.y)
                } else {
                    it.setScale(2f, event.x, event.y)
                }
            }
        }
    }

}