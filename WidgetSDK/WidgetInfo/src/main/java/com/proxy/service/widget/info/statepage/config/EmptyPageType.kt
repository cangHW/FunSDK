package com.proxy.service.widget.info.statepage.config

/**
 * @author: cangHX
 * @data: 2025/7/9 20:17
 * @desc:
 */
class EmptyPageType private constructor(val type: String) {

    companion object {
        const val WITH_OUT_REFRESH = "${PageConfig.DEFAULT}_with_out_refresh"

        fun buildDefaultPage(): EmptyPageType {
            return buildWithOutRefreshPage()
        }

        /**
         * 不带刷新按钮效果
         * */
        fun buildWithOutRefreshPage(): EmptyPageType {
            return EmptyPageType(WITH_OUT_REFRESH)
        }

        /**
         * 自定义效果
         * */
        fun buildPage(type: String): EmptyPageType {
            return EmptyPageType(type)
        }
    }

}