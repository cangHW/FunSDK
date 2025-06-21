package com.proxy.service.funsdk.imageloader

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityImageLoaderBinding
import com.proxy.service.imageloader.base.option.lottie.LottieRepeatModel

/**
 * @author: cangHX
 * @data: 2024/5/16 15:03
 * @desc:
 */
class ImageLoaderActivity : BaseActivity<ActivityImageLoaderBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ImageLoaderActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.load_net -> {
                val url =
                    "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/aaa.png"
                CsImageLoader.with(this)
                    ?.loadUrl(url)
                    ?.roundedCorners(
                        150f, 0f, 150f, 0f
                    )
                    ?.grayscale()
                    ?.fitXY()
                    ?.alpha(255)
                    ?.into(binding?.image)
            }

            R.id.load_res -> {
                CsImageLoader.with(this)
                    ?.loadRes(R.drawable.test)
                    ?.grayscale()
                    ?.into(binding?.image)
            }

            R.id.load_lottie -> {
                CsImageLoader.with(this)
                    ?.asLottieModel()
                    ?.loadRes(R.raw.test_loading)
                    ?.setRepeatCount(3)
                    ?.setRepeatModel(LottieRepeatModel.REVERSE)
                    ?.into(binding?.layout)
            }
        }
    }

}