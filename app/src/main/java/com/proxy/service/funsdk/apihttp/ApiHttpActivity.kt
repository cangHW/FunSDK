package com.proxy.service.funsdk.apihttp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.apihttp.base.init.ApiConfig
import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.apihttp.CsApi
import com.proxy.service.funsdk.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author: cangHX
 * @data: 2024/6/18 15:07
 * @desc:
 */
class ApiHttpActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ApiHttpActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_http)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.run_http_init -> {
                CsApi.init(
                    ApiConfig.builder("https://www.baidu.com")
                        .build()
                )
            }

            R.id.run_normal_http -> {
                CsApi.getService(Api::class.java)?.request()?.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        CsLogger.i("response = ${CsJsonUtils.toJson(response.body() ?: "")}")
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        CsLogger.e(t)
                    }
                })
            }
        }
    }
}