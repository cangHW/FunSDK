package com.proxy.service.camera.base.config.page.builder

import com.proxy.service.camera.base.config.page.PageConfig

/**
 * @author: cangHX
 * @data: 2026/2/8 18:23
 * @desc:
 */
interface IBuilder {

    /**
     * 构建配置
     * */
    fun build(): PageConfig

}