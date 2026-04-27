package com.proxy.service.funsdk.widget.view.centerSelect

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.camera.info.R
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewAdapter
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewHolder

/**
 * @author: cangHX
 * @data: 2026/4/24 16:35
 * @desc:
 */
class CenterSelectAdapter  : CsBaseRecyclerViewAdapter<CenterSelectViewHolder, String>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterSelectViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cs_camera_info_adapter_camera_mode, parent, false)

        rootView.setBackgroundColor(Color.WHITE)
        return CenterSelectViewHolder(rootView)
    }
}

open class CenterSelectViewHolder(itemView: View) : CsBaseRecyclerViewHolder<String>(itemView) {

    private val cameraModeName: AppCompatTextView = itemView.findViewById(R.id.cs_camera_info_mode_name)

    override fun bindData(data: String, position: Int) {
        cameraModeName.text = data
    }

}