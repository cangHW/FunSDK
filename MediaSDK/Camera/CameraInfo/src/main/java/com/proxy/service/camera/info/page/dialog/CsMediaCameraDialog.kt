package com.proxy.service.camera.info.page.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.databinding.CsCameraInfoDialogSettingOptionBinding
import com.proxy.service.widget.info.dialog.window.CsBaseDialog
import com.proxy.service.widget.info.dialog.window.info.DialogConfig
import com.proxy.service.widget.info.view.recyclerview.decoration.CsRecyclerItemDecoration

/**
 * @author: cangHX
 * @data: 2026/4/16 18:34
 * @desc:
 */
class CsMediaCameraDialog : CsBaseDialog() {

    override fun getDialogConfig(): DialogConfig {
        val config = DialogConfig()
        config.width = WindowManager.LayoutParams.MATCH_PARENT
        config.height = WindowManager.LayoutParams.MATCH_PARENT
        return config
    }

    private var titleRes: Int? = null
    private var binding: CsCameraInfoDialogSettingOptionBinding? = null

    private val adapter = CameraAdapter()

    override fun onCreateView(context: Context, parent: ViewGroup): View? {
        binding = CsCameraInfoDialogSettingOptionBinding.inflate(LayoutInflater.from(context))
        binding?.csCameraInfoPageSettingDialogRlv?.layoutManager = LinearLayoutManager(context)
        binding?.csCameraInfoPageSettingDialogRlv?.adapter = adapter
        binding?.csCameraInfoPageSettingDialogRlv?.addItemDecoration(
            CsRecyclerItemDecoration.createWithPx(1, 1)
        )
        binding?.root?.setOnClickListener {
            dismiss()
        }
        binding?.csCameraInfoPageSettingDialogCancel?.setOnClickListener {
            dismiss()
        }
        titleRes?.let {
            binding?.csCameraInfoPageSettingDialogTitle?.text = context.getText(it)
        }
        return binding?.root
    }


    fun setTitle(@StringRes strRes: Int): CsMediaCameraDialog {
        this.titleRes = strRes
        return this
    }

    fun setDataSize(count: Int): CsMediaCameraDialog {
        adapter.setDataSize(count)
        return this
    }

    fun setDataBind(call: ((text: TextView, radio: RadioButton, position: Int) -> Unit)): CsMediaCameraDialog {
        adapter.setDataBind(call)
        return this
    }

    fun setItemClick(click: ((position: Int) -> Unit)): CsMediaCameraDialog {
        adapter.setItemClick {
            dismiss()
            click.invoke(it)
        }
        return this
    }


    private class CameraAdapter : RecyclerView.Adapter<CameraAdapter.CameraViewHolder>() {

        private var count: Int = 0
        private var bindCall: ((text: TextView, radio: RadioButton, position: Int) -> Unit)? = null
        private var itemClick: ((position: Int) -> Unit)? = null

        fun setDataSize(count: Int) {
            this.count = count
        }

        fun setDataBind(call: ((text: TextView, radio: RadioButton, position: Int) -> Unit)) {
            this.bindCall = call
        }

        fun setItemClick(click: ((position: Int) -> Unit)) {
            this.itemClick = click
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cs_camera_info_dialog_setting_option_item, parent, false)
            return CameraViewHolder(view)
        }

        override fun getItemCount(): Int {
            return count
        }

        override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
            bindCall?.invoke(holder.textView, holder.radioView, position)

            holder.itemView.setOnClickListener {
                itemClick?.invoke(position)
            }
        }

        class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView = itemView.findViewById<AppCompatTextView>(R.id.dialog_option_item_text)
            val radioView =
                itemView.findViewById<AppCompatRadioButton>(R.id.dialog_option_item_radio)
        }
    }

}