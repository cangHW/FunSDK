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
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.service.document.CsDocumentImage
import com.proxy.service.document.image.base.ImageService
import com.proxy.service.document.image.base.callback.base.OnDoubleClickCallback
import com.proxy.service.document.image.base.callback.base.OnDrawCallback
import com.proxy.service.document.image.base.loader.base.IController
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.databinding.ActivityDocumentImagePreviewBinding
import com.proxy.service.funsdk.databinding.ActivityDocumentImagePreviewItemBinding

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

    private var service: ImageService? = null
    private var controller: IController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentImagePreviewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        service = CloudSystem.getService(ImageService::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.load -> {
                if (binding?.normal?.isChecked == true) {
                    binding?.imageView?.visibility = View.VISIBLE
                    binding?.pagerView?.visibility = View.GONE
                    binding?.imageView?.let {
                        it.post {
                            normalLoad(it, it.width.toFloat(), it.height.toFloat())
                        }
                    }
                } else if (binding?.viewPager?.isChecked == true) {
                    binding?.pagerView?.visibility = View.VISIBLE
                    binding?.imageView?.visibility = View.GONE
                    binding?.pagerView?.let {
                        it.post {
                            pagerLoad(it, it.width.toFloat(), it.height.toFloat())
                        }
                    }
                }
            }
        }
    }

    private fun normalLoad(layout: AppCompatImageView, width: Float, height: Float) {
        controller = createPreviewOption(width, height)?.into(layout)
    }

    private fun pagerLoad(view: ViewPager2, width: Float, height: Float) {
        val adapter = PreviewAdapter(this, width, height)
        view.adapter = adapter
    }

    private fun createPreviewOption(width: Float, height: Float): IOption? {
        val option = service?.createPreviewLoader(this)
            ?.loadRes(R.drawable.crop)

        if (binding?.doubleClickScale?.isChecked == true) {
            option?.setDoubleClickCallback(doubleClickCallback)
        }

        if (binding?.lockRect?.isChecked == true) {
            val springBack = binding?.notSpringBack?.isChecked != true
            lockRectF.set(0f, 0f, width, height)
//            lockRectF.set(300f, 300f, 800f, 800f)
            if (binding?.lockRectIsCanMove?.isChecked == true) {
//                option?.setLockSizeWithMovable(width, height, springBack)
                option?.setLockRectWithMovable(lockRectF, springBack)
            } else {
//                option?.setLockSizeWithImmovable(width, height, springBack)
                option?.setLockRectWithImmovable(lockRectF, springBack)
            }
        }

        if (binding?.showLockRect?.isChecked == true) {
            option?.setDrawCallback(drawCallback)
        }
        return option
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


    private class PreviewViewHolder(
        itemView: View,
        private val activity: ImagePreviewActivity,
        private val width: Float,
        private val height: Float
    ) : RecyclerView.ViewHolder(itemView) {

        private var binding: ActivityDocumentImagePreviewItemBinding? = null

        init {
            binding = ActivityDocumentImagePreviewItemBinding.bind(itemView)
        }

        fun bind() {
            binding?.layout?.let {
                activity.controller = activity.createPreviewOption(width, height)?.into(it)
            }
        }
    }

    private class PreviewAdapter(
        private val activity: ImagePreviewActivity,
        private val width: Float,
        private val height: Float
    ) : RecyclerView.Adapter<PreviewViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_document_image_preview_item, parent, false)
            return PreviewViewHolder(view, activity, width, height)
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
            holder.bind()
        }
    }
}