package com.proxy.service.camera.info.page.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.proxy.service.camera.info.databinding.CsCameraInfoPageActivitySettingItemBinding
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2026/4/16 16:58
 * @desc:
 */
class CameraPageSettingItemView : LinearLayoutCompat {

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {

        private var drawableRes: Int = 0

        private var title: String = ""
        private var subTitle: String? = null

        private var content: String = ""
        private var isSelect: Boolean? = null

        fun setIcon(@DrawableRes resId: Int): Builder {
            this.drawableRes = resId
            return this
        }

        fun getIcon(): Int {
            return drawableRes
        }

        fun setTitle(strRes: Int): Builder {
            this.title = CsContextManager.getApplication().getString(strRes)
            return this
        }

        fun getTitle(): String {
            return title
        }

        fun setSubTitle(strRes: Int): Builder {
            this.subTitle = CsContextManager.getApplication().getString(strRes)
            return this
        }

        fun getSubTitle(): String? {
            return subTitle
        }

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun getContent(): String {
            return content
        }

        fun setSelect(isSelect: Boolean): Builder {
            this.isSelect = isSelect
            return this
        }

        fun isSelect(): Boolean? {
            return isSelect
        }

        fun build(): Builder {
            return this
        }
    }


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var binding: CsCameraInfoPageActivitySettingItemBinding? = null
    private var onViewClickListener: ((view: CameraPageSettingItemView) -> Unit)? = null
    private var onViewSelectListener: ((isSelect: Boolean) -> Unit)? = null

    private fun init(context: Context) {
        binding = CsCameraInfoPageActivitySettingItemBinding
            .inflate(LayoutInflater.from(context), this, true)
    }


    fun show(builder: Builder) {
        binding?.csMediaCameraSettingItemIcon?.setImageResource(builder.getIcon())

        binding?.csMediaCameraSettingItemTitle?.text = builder.getTitle()
        builder.getSubTitle()?.let {
            binding?.csMediaCameraSettingItemSubTitle?.visibility = View.VISIBLE
            binding?.csMediaCameraSettingItemSubTitle?.text = it
        }

        builder.isSelect()?.let {
            binding?.csMediaCameraSettingItemTextLayout?.visibility = View.GONE
            binding?.csMediaCameraSettingItemTextSwitch?.visibility = View.VISIBLE
            binding?.csMediaCameraSettingItemTextSwitch?.isSelected = it

            binding?.csMediaCameraSettingItemTextSwitch?.setOnCheckedChangeListener { _, isChecked ->
                onViewSelectListener?.invoke(isChecked)
            }
        } ?: let {
            binding?.csMediaCameraSettingItemTextSwitch?.visibility = View.GONE
            binding?.csMediaCameraSettingItemTextLayout?.visibility = View.VISIBLE
            binding?.csMediaCameraSettingItemTextContent?.text = builder.getContent()
        }

        binding?.root?.setOnClickListener {
            onViewClickListener?.invoke(this)
        }
    }

    fun updateContent(content: String) {
        if (binding?.csMediaCameraSettingItemTextContent?.visibility == View.VISIBLE) {
            binding?.csMediaCameraSettingItemTextContent?.text = content
        }
    }


    fun setOnViewClickListener(listener: ((view: CameraPageSettingItemView) -> Unit)) {
        this.onViewClickListener = listener
    }

    fun setOnViewSelectListener(listener: ((isSelect: Boolean) -> Unit)) {
        this.onViewSelectListener = listener
    }
}