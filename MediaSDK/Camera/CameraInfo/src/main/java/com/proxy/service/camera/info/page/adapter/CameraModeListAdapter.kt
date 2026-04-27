package com.proxy.service.camera.info.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.info.R
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewAdapter
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewHolder

/**
 * @author: cangHX
 * @data: 2026/2/11 14:28
 * @desc:
 */
class CameraModeListAdapter : CsBaseRecyclerViewAdapter<CameraModeListViewHolder, CameraFunMode>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraModeListViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cs_camera_info_adapter_camera_mode, parent, false)

        return CameraModeListViewHolder(rootView)
    }
}

open class CameraModeListViewHolder(itemView: View) : CsBaseRecyclerViewHolder<CameraFunMode>(itemView) {

    private val cameraModeName: AppCompatTextView = itemView.findViewById(R.id.cs_camera_info_mode_name)

    override fun bindData(data: CameraFunMode, position: Int) {
        cameraModeName.text = data.getModeName()
    }

}

