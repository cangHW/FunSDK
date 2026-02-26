package com.proxy.service.camera.info.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.R
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewAdapter
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewHolder

/**
 * @author: cangHX
 * @data: 2026/2/11 14:28
 * @desc:
 */
class CameraModeListAdapter : CsBaseRecyclerViewAdapter<CameraModeListViewHolder, CameraMode>() {

    private var cameraModeClickListener: OnItemClickListener<CameraMode>? = null

    fun setOnCameraModeClickListener(listener: OnItemClickListener<CameraMode>) {
        this.cameraModeClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraModeListViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cs_camera_info_page_camera_mode, parent, false)

        return CameraModeListViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: CameraModeListViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.cameraModeRes.setOnClickListener {
            try {
                holder.getBindData()?.let {
                    cameraModeClickListener?.onItemClick(it)
                }
            } catch (throwable: Throwable) {
                CsLogger.e(throwable)
            }
        }
    }

}

open class CameraModeListViewHolder(itemView: View) : CsBaseRecyclerViewHolder<CameraMode>(itemView) {

    private val cameraModeName: AppCompatTextView = itemView.findViewById(R.id.camera_mode_name)
    val cameraModeRes: AppCompatImageView = itemView.findViewById(R.id.camera_mode_res)

    override fun bindData(data: CameraMode, position: Int) {
        cameraModeName.text = data.getModeName()
        cameraModeRes.setImageResource(data.getModeRes())
    }

}

