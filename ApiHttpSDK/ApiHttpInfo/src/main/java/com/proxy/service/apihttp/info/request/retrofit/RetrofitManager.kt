package com.proxy.service.apihttp.info.request.retrofit

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.request.okhttp.OkhttpManager
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

    private const val TAG = "${Constants.LOG_REQUEST_TAG_START}Init"

    @Volatile
    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        var retrofit: Retrofit? = retrofit
        if (retrofit != null) {
            return retrofit
        }
        synchronized(this) {
            retrofit = RetrofitManager.retrofit
            if (retrofit != null) {
                return retrofit as Retrofit
            }
            retrofit = create()
            RetrofitManager.retrofit = retrofit
            return retrofit!!
        }
    }

    fun clear() {
        retrofit = null
    }

    private fun create(): Retrofit {
        val config = Config.getRequestConfig()
        val builder = Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .client(OkhttpManager.create())

        config.getConverterFactory().forEach {
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