package com.proxy.service.camera.info.page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.R
import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2026/2/10 14:49
 * @desc:
 */
class CameraModeAdapter : PagerAdapter() {

    private val itemWidth = CsDpUtils.dp2pxf(70f)

    private val modes = ArrayList<CameraMode>()

    /**
     * 设置相机模式
     * */
    fun setCameraModes(list: ArrayList<CameraMode>?) {
        if (list == null) {
            return
        }
        this.modes.clear()
        this.modes.addAll(list)
        notifyDataSetChanged()
    }

    override fun getPageWidth(position: Int): Float {
//        return itemWidth
        return 0.1f
    }

    override fun getCount(): Int {
        return modes.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val rootView = LayoutInflater.from(container.context)
            .inflate(R.layout.cs_camera_info_page_camera_mode, container, false)

        val cameraModeName = rootView.findViewById<AppCompatTextView>(R.id.camera_mode_name)
        val cameraModeRes = rootView.findViewById<AppCompatImageView>(R.id.camera_mode_res)

        modes.getOrNull(position)?.let {
            cameraModeName.text = it.getModeName()
            cameraModeRes.setImageResource(it.getModeRes())
        }

        container.addView(rootView)

        return rootView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as? View?)
    }
}