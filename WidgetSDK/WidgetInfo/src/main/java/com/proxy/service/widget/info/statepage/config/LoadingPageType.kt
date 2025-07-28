package com.proxy.service.widget.info.statepage.config

/**
 * @author: cangHX
 * @data: 2025/7/9 20:20
 * @desc:
 */
class LoadingPageType private constructor(val key: String) {

    companion object {
        const val ROTATION = "${PageConfig.DEFAULT}_rotation"

        fun buildDefaultPage(): LoadingPageType {
            return buildRotationPage()
        }

        /**
         * 旋转效果
         * */
        fun buildRotationPage(): LoadingPageType {
            return LoadingPageType(ROTATION)
        }

        /**
         * 自定义效果
         *
         * @param key 唯一标识
         * */
        fun buildPage(key: String): LoadingPageType {
            return LoadingPageType(key)
        }
    }

}