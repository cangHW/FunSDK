package com.proxy.service.core.framework.ui.monitor.visible.config

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.ui.monitor.visible.base.IVisibleBuilder
import com.proxy.service.core.framework.ui.monitor.visible.base.IVisibleBuilderGet
import com.proxy.service.core.framework.ui.monitor.visible.callback.VisibleMonitorCallback
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/4 18:33
 * @desc:
 */
class VisibleMonitorConfig private constructor(
    private val builder: IVisibleBuilderGet
) : IVisibleBuilderGet {

    override fun getBindView(): View {
        return builder.getBindView()
    }

    override fun getLifecycleOwner(): LifecycleOwner? {
        return builder.getLifecycleOwner()
    }

    override fun getArea(): Float {
        return builder.getArea()
    }

    override fun getDuration(): Long {
        return builder.getDuration()
    }

    override fun getDelayMillis(): Long {
        return builder.getDelayMillis()
    }

    override fun getTag(): Any? {
        return builder.getTag()
    }

    companion object {
        fun builder(view: View): IVisibleBuilder {
            return Builder(view)
        }
    }

    private class Builder(private val view: View) : IVisibleBuilder, IVisibleBuilderGet {

        private var area: Float = VisibleConfig.DEFAULT_AREA
        private var duration: Long = VisibleConfig.DEFAULT_DURATION
        private var delayMillis: Long = VisibleConfig.DEFAULT_DELAY_MILLIS

        private var lifecycleOwner: LifecycleOwner?=null
        private var tag: Any? = null


        override fun setLifecycle(lifecycleOwner: LifecycleOwner): IVisibleBuilder {
            this.lifecycleOwner = lifecycleOwner
            return this
        }

        override fun setArea(area: Float): IVisibleBuilder {
            if (area > 0 && area <= 1) {
                this.area = area
            }
            return this
        }

        override fun setDuration(duration: Long, unit: TimeUnit): IVisibleBuilder {
            unit.toMillis(duration).let {
                if (it >= 0) {
                    this.duration = it
                }
            }
            return this
        }

        override fun setDelayMillis(delayMillis: Long, unit: TimeUnit): IVisibleBuilder {
            unit.toMillis(delayMillis).let {
                if (it >= 0) {
                    this.delayMillis = it
                }
            }
            return this
        }

        override fun setTag(tag: Any): IVisibleBuilder {
            this.tag = tag
            return this
        }

        override fun build(): VisibleMonitorConfig {
            return VisibleMonitorConfig(this)
        }

        override fun getBindView(): View {
            return view
        }

        override fun getLifecycleOwner(): LifecycleOwner? {
            return lifecycleOwner
        }

        override fun getArea(): Float {
            return this.area
        }

        override fun getDuration(): Long {
            return this.duration
        }

        override fun getDelayMillis(): Long {
            return this.delayMillis
        }

        override fun getTag(): Any? {
            return this.tag
        }
    }

}