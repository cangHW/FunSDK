package com.proxy.service.widget.info.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.widget.info.statepage.CsStatePageManager
import com.proxy.service.widget.info.statepage.config.EmptyPageType
import com.proxy.service.widget.info.statepage.config.ErrorPageType
import com.proxy.service.widget.info.statepage.config.IStatePageController
import com.proxy.service.widget.info.statepage.config.LoadingPageType

/**
 * @author: cangHX
 * @data: 2026/4/9 16:23
 * @desc:
 */
abstract class CsBaseActivity<T : ViewBinding> : FragmentActivity() {

    protected var binding: T? = null
    protected var statePage: IStatePageController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isNavigationBarTransparentEnable()) {
            CsBarUtils.setNavigationBarTransparent(this)
        }
        if (isStatusBarTransparentEnable()) {
            CsBarUtils.setStatusBarTransparent(this, isStatusBarDarkModelEnable())
        }
        super.onCreate(savedInstanceState)
        binding = getViewBinding(LayoutInflater.from(this))

        binding?.let {
            statePage = CsStatePageManager.inflate(
                it.root,
                loadingPageType = getLoadingPageType(),
                errorPageType = getErrorPageType(),
                emptyPageType = getEmptyPageType()
            )
            setContentView(statePage?.getRootView())
        }

        initView()
        initData(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initData(intent)
    }

    /**
     * 是否透明状态栏
     * */
    protected open fun isStatusBarTransparentEnable(): Boolean {
        return true
    }

    /**
     * 是否状态栏暗夜模式, 黑夜模式下状态栏字体颜色为白色
     * */
    protected open fun isStatusBarDarkModelEnable(): Boolean {
        return false
    }

    /**
     * 是否透明导航栏
     * */
    protected open fun isNavigationBarTransparentEnable(): Boolean {
        return true
    }

    /**
     * 获取 loading 页面类型
     * */
    protected open fun getLoadingPageType(): LoadingPageType {
        return LoadingPageType.buildDefaultPage()
    }

    /**
     * 获取 empty 页面类型
     * */
    protected open fun getEmptyPageType(): EmptyPageType {
        return EmptyPageType.buildDefaultPage()
    }

    /**
     * 获取 error 页面类型
     * */
    protected open fun getErrorPageType(): ErrorPageType {
        return ErrorPageType.buildDefaultPage()
    }

    /**
     * 获取 ViewBinding 对象
     * */
    abstract fun getViewBinding(inflater: LayoutInflater): T

    /**
     * 初始化 view
     * */
    protected open fun initView() {}

    /**
     * 初始化数据
     * */
    protected open fun initData(intent: Intent?) {}
}