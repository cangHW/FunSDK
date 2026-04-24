package com.proxy.service.funsdk.widget.view.centerSelect

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetCenterSelectBinding
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.view.recyclerview.CsCenterSelectRecyclerView
import com.proxy.service.widget.info.view.recyclerview.decoration.CsRecyclerItemDecoration

/**
 * @author: cangHX
 * @data: 2026/4/24 16:28
 * @desc:
 */
class CenterSelectActivity : BaseActivity<ActivityWidgetCenterSelectBinding>(),
    CsCenterSelectRecyclerView.OnSelectionChangedListener {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CenterSelectActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val centerSelectAdapter = CenterSelectAdapter()

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetCenterSelectBinding {
        return ActivityWidgetCenterSelectBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()

        val list = arrayListOf(
            "张1", "张2", "张3", "张4", "张5", "张6", "张7", "张8", "张9",
            "里斯", "王五", "赵流", "张龙", "招呼", "王朝", "马汉"
        )
        centerSelectAdapter.setData(list)

        binding?.centerSelect?.adapter = centerSelectAdapter
        binding?.centerSelect?.setOnSelectionChangedListener(this)

        binding?.centerSelect?.setFlingEnabled(false)
        binding?.centerSelect?.setSelectedPosition(0, false)
        binding?.centerSelect?.addItemDecoration(
            CsRecyclerItemDecoration.builder()
                .horizontalSpaceDp(3f)
                .build()
        )
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.index_1 -> {
                binding?.centerSelect?.setSelectedPosition(1, true)
            }

            R.id.index_2 -> {
                binding?.centerSelect?.setSelectedPosition(3, false)
            }

            R.id.index_3 -> {
                binding?.centerSelect?.setSelectedPosition(5, true)
            }
        }
    }

    override fun onSelectionChanged(oldPosition: Int, newPosition: Int, fromUser: Boolean) {
        CsToast.show("select oldPosition=$oldPosition, newPosition=$newPosition, fromUser=$fromUser")
    }
}