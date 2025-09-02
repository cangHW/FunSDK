package com.proxy.service.apihttp.info.request.retrofit

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.info.common.okhttp.OkhttpFactory
import com.proxy.service.apihttp.info.request.okhttp.OkhttpConfigImpl
import com.proxy.service.apihttp.info.request.retrofit.converter.gson.GsonConverterFactory
import com.proxy.service.apihttp.info.request.retrofit.converter.scalars.ScalarsConverterFactory
import com.proxy.service.core.framework.data.log.CsLogger
import retrofit2.Retrofit

/**
 * @author: cangHX
 * @data: 2024/5/21 17:57
 * @desc:
 */
object RetrofitManager {

    private const val TAG = "${ApiConstants.LOG_REQUEST_TAG_START}Init"

    fun getRetrofit(config: RequestConfig): Retrofit {
        val client = OkhttpFactory.create(OkhttpConfigImpl(config))

        val builder = Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .client(client)

        config.getConverterFactory().forEach {
            CsLogger.tag(TAG).d("addConverterFactory factory = $it")
            builder.addConverterFactory(it)
        }
        builder.addConverterFactory(ScalarsConverterFactory.create())
        builder.addConverterFactory(GsonConverterFactory.create())

        config.getCallAdapterFactory().forEach {
            CsLogger.tag(TAG).d("addCallAdapterFactory factory = $it")
            builder.addCallAdapterFactory(it)
        }

        return builder.build()
    }
}