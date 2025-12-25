package com.proxy.service.widget.info.presenter

import androidx.annotation.CallSuper
import com.proxy.service.widget.info.presenter.core.BasePresenter

/**
 * @author: cangHX
 * @data: 2025/12/25 11:02
 * @desc:
 */
abstract class CsBasePresenter: BasePresenter() {

    @CallSuper
    override fun onCreate() {

    }

    @CallSuper
    override fun onStart() {

    }

    @CallSuper
    override fun onResume() {

    }

    @CallSuper
    override fun onPause() {

    }

    @CallSuper
    override fun onStop() {

    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

}