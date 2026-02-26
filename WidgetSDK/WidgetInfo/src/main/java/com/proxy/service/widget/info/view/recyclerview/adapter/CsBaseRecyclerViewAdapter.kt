package com.proxy.service.widget.info.view.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.constants.WidgetConstants

/**
 * @author: cangHX
 * @data: 2026/2/11 17:29
 * @desc:
 */
abstract class CsBaseRecyclerViewAdapter<VH : CsBaseRecyclerViewHolder<DATA>, DATA : Any> :
    RecyclerView.Adapter<VH>() {

    companion object {
        private const val TAG = "${WidgetConstants.TAG}RecyclerAdapter"
    }

    interface OnItemClickListener<DATA> {
        fun onItemClick(data: DATA)
    }

    private val dataList = ArrayList<DATA>()

    private var itemClickListener: OnItemClickListener<DATA>? = null

    /**
     * 设置 item 点击回调
     * */
    open fun setOnItemClickListener(listener: OnItemClickListener<DATA>) {
        this.itemClickListener = listener
    }

    /**
     * 设置数据
     * */
    @SuppressLint("NotifyDataSetChanged")
    open fun setData(list: List<DATA>?) {
        this.dataList.clear()
        list?.let {
            this.dataList.addAll(it)
        }
        notifyDataSetChanged()
    }

    /**
     * 获取数据
     * */
    open fun getData(isCopy: Boolean = false): ArrayList<DATA> {
        if (isCopy) {
            val list = ArrayList<DATA>()
            list.addAll(dataList)
            return list
        }
        return dataList
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = dataList.getOrNull(position)

        if (data == null) {
            CsLogger.tag(TAG).e("data exception. position=$position, dataList=$dataList")
            return
        }

        holder.itemView.setOnClickListener {
            try {
                itemClickListener?.onItemClick(data)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }

        try {
            holder.setBindData(data, position)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

}

abstract class CsBaseRecyclerViewHolder<DATA : Any>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private var _bindData: DATA? = null

    /**
     * 获取当前绑定数据
     * */
    open fun getBindData(): DATA? {
        return _bindData
    }

    /**
     * 绑定数据
     * */
    open fun setBindData(data: DATA, position: Int) {
        this._bindData = data
        bindData(data, position)
    }

    /**
     * 绑定数据
     * */
    protected abstract fun bindData(data: DATA, position: Int)

}





