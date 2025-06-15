package com.proxy.service.funsdk.document.image.crop

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.document.CsDocumentImage
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.document.image.base.callback.crop.OnCropCallback
import com.proxy.service.document.image.base.loader.crop.ICropController
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.databinding.ActivityDocumentImageCropBinding

/**
 * @author: cangHX
 * @data: 2025/6/4 20:03
 * @desc:
 */
class ImageCropActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ImageCropActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var binding: ActivityDocumentImageCropBinding? = null

    private var controller: ICropController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentImageCropBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        val layout = findViewById<FrameLayout>(R.id.layout)

        controller = CsDocumentImage.createCropLoader(this)
            ?.loadRes(R.drawable.crop)
            ?.setCropSize(600f, 600f)
            ?.setCropLineColor(Color.RED)
            ?.into(layout)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.cover_layout -> {
                binding?.coverLayout?.visibility = View.GONE
            }

            R.id.crop -> {
                controller?.startCrop(object : OnCropCallback {
                    override fun onCropResult(status: Int, bitmap: Bitmap?) {
                        CsLogger.i("onCropResult status=$status, bitmap=$bitmap")
                        if (bitmap == null) {
                            return
                        }
                        binding?.coverLayout?.visibility = View.VISIBLE
                        CsImageLoader.with(this@ImageCropActivity)
                            ?.loadBitmap(bitmap)
                            ?.into(binding?.coverImage)
                    }
                })
            }
        }
    }

}