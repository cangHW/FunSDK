package com.proxy.service.document.pdf.base.view.callback

import android.widget.FrameLayout

/**
 * @author: cangHX
 * @data: 2025/7/24 20:13
 * @desc:
 */
interface OnShouldCreateCoverViewCallback {

    /**
     * 展示覆盖 view
     *
     * @param position      页码
     * @param coverLayout   遮罩父布局
     * */
    fun onShouldShowCoverView(position: Int, coverLayout: FrameLayout)

}