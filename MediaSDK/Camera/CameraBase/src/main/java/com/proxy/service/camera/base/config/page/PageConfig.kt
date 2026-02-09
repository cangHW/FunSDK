package com.proxy.service.camera.base.config.page

import com.proxy.service.camera.base.config.page.builder.IBuilder
import com.proxy.service.camera.base.config.page.builder.IBuilderGet


/**
 * @author: cangHX
 * @data: 2026/2/4 16:12
 * @desc:
 */
class PageConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet {

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        override fun build(): PageConfig {
            return PageConfig(this)
        }

    }

}