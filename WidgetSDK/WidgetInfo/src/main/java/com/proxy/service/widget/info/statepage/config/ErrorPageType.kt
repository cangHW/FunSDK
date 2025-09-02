package com.proxy.service.widget.info.statepage.config

/**
 * @author: cangHX
 * @data: 2025/7/9 20:19
 * @desc:
 */
class ErrorPageType private constructor(val key: String) {

    companion object {

        const val WITH_REFRESH = "${PageConfig.DEFAULT}_with_refresh"

        fun buildDefaultPage(): ErrorPageType {
            return buildWithRefreshPage()
        }

        /**
         * 带按钮效果
         * */
        fun buildWithRefreshPage(): ErrorPageType {
            return ErrorPageType(WITH_REFRESH)
        }

        /**
         * 自定义效果
         *
         * @param key 唯一标识
         * */
        fun buildPage(key: String): ErrorPageType {
            return ErrorPageType(key)
        }
    }

}