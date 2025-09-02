package com.proxy.service.funsdk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.proxy.service.widget.info.statepage.CsStatePageManager
import com.proxy.service.widget.info.statepage.config.IStatePageController

/**
 * @author: cangHX
 * @data: 2025/6/17 11:48
 * @desc:
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected var binding: T? = null
    protected var statePage: IStatePageController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding(LayoutInflater.from(this))

        binding?.let {
            statePage = CsStatePageManager.inflate(it.root)
            setContentView(statePage?.getRootView())
        }

        initView()
    }

    abstract fun getViewBinding(inflater: LayoutInflater): T

    protected open fun initView() {}

    abstract fun onClick(view: View)
}