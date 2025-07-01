package com.proxy.service.funsdk.document.image.crop

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.document.image.base.ImageService
import com.proxy.service.document.image.base.callback.crop.OnCropCallback
import com.proxy.service.document.image.base.loader.crop.ICropController
import com.proxy.service.document.image.base.mode.CropMode
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

    private var service: ImageService? = null
    private var controller: ICropController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentImageCropBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        service = CloudSystem.getService(ImageService::class.java)
        binding?.layout?.let {
            controller = service?.createCropLoader(this)
                ?.loadRes(R.drawable.crop)
//                ?.setCropFrameRectToFitBitmap()
                ?.setCropFrameSize(600f, 300f)
//                ?.setCropFrameRect(RectF(1060f, 20f, 1100f, 100f))
                ?.setCropFrameLineWidth(20f)
                ?.setCropFrameLineColor(Color.parseColor("#66ff0000"))
                ?.setCropMode(CropMode.builderCropFrameMoveAndScaleMode().setCropFrameAspectRatioLock(true).build())
                ?.into(it)
        }
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
                            Toast.makeText(this@ImageCropActivity, "失败 $status", Toast.LENGTH_SHORT).show()
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