package com.proxy.service.funsdk.document.image.preview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.service.document.CsDocumentImage
import com.proxy.service.document.base.image.callback.base.OnDoubleClickCallback
import com.proxy.service.document.base.image.callback.base.OnDrawCallback
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

    private val lockRectF = RectF(0f, 0f, 0f, 0f)
    private var binding: ActivityDocumentImagePreviewBinding? = null

    private var controller: IController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentImagePreviewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.load -> {
                binding?.layout?.let {
                    it.post {
                        if (binding?.normal?.isChecked == true) {
                            normalLoad(it, it.width.toFloat(), it.height.toFloat())
                        }
                    }
                }
            }
        }
    }

    private fun normalLoad(layout: FrameLayout, width: Float, height: Float) {
        val option = CsDocumentImage.createPreviewLoader(this)
            ?.loadRes(R.drawable.crop)

        if (binding?.doubleClickScale?.isChecked == true) {
            option?.setDoubleClickCallback(doubleClickCallback)
        }

        if (binding?.lockRect?.isChecked == true) {
            val springBack = binding?.notSpringBack?.isChecked != true
            lockRectF.set(0f, 0f, width, height)
//            lockRectF.set(300f, 300f, 800f, 800f)
            if (binding?.lockRectIsCanMove?.isChecked == true) {
                option?.setLockRectWithMovable(lockRectF, springBack)
            } else {
                option?.setLockRectWithImmovable(lockRectF, springBack)
            }
        }

        if (binding?.showLockRect?.isChecked == true) {
            option?.setDrawCallback(drawCallback)
        }

        controller = option?.into(layout)
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

    private val drawCallback = object : OnDrawCallback {
        override fun onDraw(
            bitmapRect: RectF,
            matrix: Matrix,
            canvas: Canvas,
            paint: Paint,
            width: Int,
            height: Int
        ) {
            paint.color = Color.RED
            paint.strokeWidth = 10f
            paint.style = Paint.Style.STROKE
            canvas.drawRect(lockRectF, paint)
        }
    }
}