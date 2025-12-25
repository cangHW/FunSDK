package com.proxy.service.widget.info.presenter

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author: cangHX
 * @data: 2025/12/25 10:51
 * @desc: 类 mvp 架构, 用于生成自带生命周期的 presenter, 灵活管理 view 模块
 */
class CsPresenterProvider {

    companion object {
        fun builder(rootView: ViewGroup): Builder {
            return Builder(rootView)
        }
    }

    class Builder(private val rootView: ViewGroup) {

        private var activity: Activity? = null
        private var fragment: Fragment? = null
        private var lifecycleOwner: LifecycleOwner? = null
        private var viewModelStoreOwner: ViewModelStoreOwner? = null

        /**
         * 绑定 activity
         * */
        fun bindActivity(activity: Activity?): Builder {
            this.activity = activity
            return this
        }

        /**
         * 绑定 fragment
         * */
        fun bindFragment(fragment: Fragment?): Builder {
            this.fragment = fragment
            return this
        }

        /**
         * 绑定 LifecycleOwner
         * */
        fun bindLifecycleOwner(owner: LifecycleOwner?): Builder {
            this.lifecycleOwner = owner
            return this
        }

        /**
         * 绑定 ViewModelStoreOwner
         * */
        fun bindViewModelStoreOwner(owner: ViewModelStoreOwner?): Builder {
            this.viewModelStoreOwner = owner
            return this
        }

        fun <T : CsBasePresenter> build(tClass: Class<T>): T {
            val t = tClass.getDeclaredConstructor().newInstance()
            t.bindActivity(activity)
            t.bindFragment(fragment)
            t.bindViewModelStoreOwner(viewModelStoreOwner)
            t.bindLifecycleOwner(lifecycleOwner)
            t.bindRootView(rootView)
            return t
        }
    }
}