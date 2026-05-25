package com.proxy.service.apm.info.sampler

/**
 * @author: cangHX
 * @date: 2026/5/22 15:39
 * @desc:
 */
interface ICompositeSamplerData {

    /**
     * 标题数据格式化
     * */
    fun formatTitle(builder: StringBuilder, index: Int, total: Int)

    /**
     * 内容数据格式化
     * */
    fun formatContent(builder: StringBuilder)

}