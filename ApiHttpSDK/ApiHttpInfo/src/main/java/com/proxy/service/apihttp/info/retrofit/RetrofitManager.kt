package com.proxy.service.apihttp.info.retrofit

import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.okhttp.OkhttpManager
import com.proxy.service.apihttp.info.retrofit.converter.gson.GsonConverterFactory
import com.proxy.service.apihttp.info.retrofit.converter.scalars.ScalarsConverterFactory
import retrofit2.Retrofit

/**
 * @author: cangHX
 * @data: 2024/5/21 17:57
 * @desc:
 */
object RetrofitManager {

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
            return retrofit as Retrofit
        }
    }

    fun clear() {
        retrofit = null
    }

    private fun create(): Retrofit {
        val config = Config.getApiConfig()
        val builder = Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .client(OkhttpManager.create())

        config.getConverterFactory().forEach {
            builder.addConverterFactory(it)
        }
        builder.addConverterFactory(ScalarsConverterFactory.create())
        builder.addConverterFactory(GsonConverterFactory.create())

        config.getCallAdapterFactory().forEach {
            builder.addCallAdapterFactory(it)
        }

        return builder.build()
    }
}