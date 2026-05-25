package com.proxy.service.apm.info.sampler

/**
 * @author: cangHX
 * @date: 2026/5/22 15:39
 * @desc:
 */
interface SamplerData {

    /**
     * 标题数据格式化
     * */
    fun formatTitle(builder: StringBuilder, total: Int = 1)

    /**
     * 内容数据格式化
     * */
    fun formatContent(builder: StringBuilder, index: Int = 0, total: Int = 1)

}