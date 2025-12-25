package com.proxy.service.widget.info.presenter.core

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author: cangHX
 * @data: 2025/12/25 16:40
 * @desc:
 */
abstract class BasePresenter : ILifecycle {

    protected var context: Context? = null

    protected var rootParentViewGroup: ViewGroup? = null
    protected var rootView: View? = null

    protected var activity: Activity? = null
    protected var fragment: Fragment? = null

    protected var lifecycleOwner: LifecycleOwner? = null
    protected var viewModelStoreOwner: ViewModelStoreOwner? = null

    @CallSuper
    open fun bindActivity(activity: Activity?) {
        this.activity = activity
    }

    @CallSuper
    open fun bindFragment(fragment: Fragment?) {
        this.fragment = fragment
    }

    @CallSuper
    open fun bindLifecycleOwner(owner: LifecycleOwner?) {
        this.lifecycleOwner = owner

        lifecycleOwner?.lifecycle?.addObserver(LifecycleController(this))
    }

    @CallSuper
    open fun bindViewModelStoreOwner(owner: ViewModelStoreOwner?) {
        this.viewModelStoreOwner = owner
    }

    fun bindRootView(viewGroup: ViewGroup) {
        this.rootParentViewGroup = viewGroup
        val context = viewGroup.context
        this.context = context
        viewGroup.removeAllViews()
        val childView = onCreateView(context, LayoutInflater.from(context), viewGroup)
        childView?.let {
            if (it.parent == null) {
                viewGroup.addView(it)
            }
        }
        rootView = childView
    }


    /**
     * 创建 view
     * */
    abstract fun onCreateView(context: Context, inflater: LayoutInflater, container: ViewGroup): View?


    override fun onDestroy() {
        this.activity = null
        this.fragment = null
        this.lifecycleOwner = null
        this.viewModelStoreOwner = null
    }
}