package com.proxy.service.funsdk.apihttp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.apihttp.download.ApiDownloadActivity
import com.proxy.service.funsdk.apihttp.request.ApiRequestActivity

/**
 * @author: cangHX
 * @data: 2024/11/5 13:30
 * @desc:
 */
class ApiActivity: AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ApiActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.api_request -> {
                ApiRequestActivity.launch(this)
            }

            R.id.api_download -> {
                ApiDownloadActivity.launch(this)
            }

            R.id.api_upload -> {
            }
        }
    }

}