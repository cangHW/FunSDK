package com.proxy.service.funsdk.apihttp.request

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.apihttp.CsApi
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityApiRequestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author: cangHX
 * @data: 2024/6/18 15:07
 * @desc:
 */
class ApiRequestActivity : BaseActivity<ActivityApiRequestBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ApiRequestActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.run_http_init -> {
                CsApi.init(
                    RequestConfig.builder("https://www.baidu.com")
                        .build()
                )
                binding?.content?.addData("API", "初始化")
            }

            R.id.run_normal_http -> {
                binding?.content?.addData("API", "普通请求")
                CsApi.getService(Api::class.java)?.request()
                    ?.enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            binding?.content?.addData(
                                "API",
                                "onResponse response = ${CsJsonUtils.toJson(response.body() ?: "")}"
                            )
                        }

                        override fun onFailure(call: Call<ApiResponse>, throwable: Throwable) {
                            binding?.content?.addData("API", "onFailure throwable = $throwable")
                        }
                    })
            }
        }
    }
}