package com.proxy.service.apm.info.monitor.anr.report

import com.proxy.service.apm.info.sampler.ICompositeSamplerData

class AnrReport(
    val time: Long,
    val samplerData: List<ICompositeSamplerData>
)
