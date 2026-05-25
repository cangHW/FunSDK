package com.proxy.service.apm.info.sampler.composite

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.ICompositeSamplerData
import com.proxy.service.apm.info.sampler.SamplerData

/**
 * @author: cangHX
 * @date: 2026/5/25 10:28
 * @desc:
 */
class CompositeSamplerData(
    private val list: List<SamplerData>
) : ICompositeSamplerData {

    fun getSnapshots(): List<SamplerData> = list

    override fun formatTitle(builder: StringBuilder, index: Int, total: Int) {
        builder.append(Constants.DIVIDER_HALF)
        builder.append(" 采样器 ${index + 1}/$total ")
        builder.append(Constants.DIVIDER_HALF)
        builder.append("\n")
        list.getOrNull(0)?.formatTitle(builder, list.size)
    }

    override fun formatContent(builder: StringBuilder) {
        val totalNum = list.size
        list.forEachIndexed { childIndex, samplerData ->
            samplerData.formatContent(builder, childIndex, totalNum)
        }
    }
}