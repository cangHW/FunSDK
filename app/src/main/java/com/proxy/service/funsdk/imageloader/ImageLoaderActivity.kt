package com.proxy.service.funsdk.imageloader

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatRadioButton
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityImageLoaderBinding
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationUpdateCallback
import com.proxy.service.imageloader.base.option.pag.image.PagImageData
import com.proxy.service.imageloader.base.option.pag.image.PagScaleMode
import com.proxy.service.imageloader.base.option.pag.scene.PagSceneMode
import com.proxy.service.imageloader.base.option.pag.txt.PagTextData
import com.proxy.service.imageloader.base.option.pag.txt.PagTextFont

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val from = arrayOf(
            binding?.isFromNetWork,
            binding?.isFromRes
        )
        setSelect(from)
        from[0]?.isChecked = true

        val types = arrayOf(
            binding?.isJpg,
            binding?.isGif,
            binding?.isPng1,
            binding?.isWebp1,
            binding?.isWebp2,
            binding?.isLottie,
            binding?.isPag
        )
        setSelect(types)
        types[0]?.isChecked = true
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityImageLoaderBinding {
        return ActivityImageLoaderBinding.inflate(inflater)
    }

    private fun setSelect(views: Array<AppCompatRadioButton?>) {
        views.forEach {
            it?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    views.forEach { view ->
                        if (view != buttonView) {
                            view?.isChecked = false
                        }
                    }
                }
                buttonView.isChecked = isChecked
            }
        }
    }

    private val jpg = ResourceInfo(
        "https://img1.baidu.com/it/u=2172818577,3783888802&fm=253&app=138&f=JPEG?w=800&h=1422",
        R.drawable.jpg
    )

    private val gif = ResourceInfo(
        "https://p6.itc.cn/q_70/images03/20210120/a0a8e62215a6416bb805292ac52b0d3b.gif",
        0
    )

    private val png1 = ResourceInfo(
        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/aaa.png",
        R.drawable.png
    )

    private val webp1 = ResourceInfo(
        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/%E5%9B%BE%E7%89%87/test.webp",
        R.drawable.webp
    )

    private val webp2 = ResourceInfo(
        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/%E5%9B%BE%E7%89%87/4e52c6665db400f514e6b52d00bc12f5021f100e8261-3GsIwm_fw658.webp",
        R.drawable.webp_anim
    )

    private val lottie = ResourceInfo(
//        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/%E5%9B%BE%E7%89%87/lottie_loading.json",
//        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/test_lottie/loading.zip",
        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/test_lottie/motivate_3.1_lottie.zip",
//        "https://static0.xesimg.com/newgameresources/qd_cs_bx1/motivate_3.1_lottie.json",
        R.raw.lottie_loading
    )

    private val pag = ResourceInfo(
        "https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/%E5%9B%BE%E7%89%87/one.pag",
        R.raw.one
    )

    override fun onClick(view: View) {
        binding?.image?.visibility = View.INVISIBLE
        binding?.layout?.visibility = View.INVISIBLE

        when (view.id) {
            R.id.load -> {
                if (binding?.isJpg?.isChecked == true) {
                    loadJpg(jpg)
                } else if (binding?.isPng1?.isChecked == true) {
                    loadPng1(png1)
                } else if (binding?.isGif?.isChecked == true) {
                    loadGif(gif)
                } else if (binding?.isWebp1?.isChecked == true) {
                    loadWebp1(webp1)
                } else if (binding?.isWebp2?.isChecked == true) {
                    loadWebp2(webp2)
                } else if (binding?.isLottie?.isChecked == true) {
                    loadLottie(lottie)
                } else if (binding?.isPag?.isChecked == true) {
                    loadPag(pag)
                }
            }
        }
    }

    private fun loadJpg(info: ResourceInfo) {
        binding?.image?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)

        if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)?.into(binding?.image)
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)?.into(binding?.image)
        }
    }

    private fun loadGif(info: ResourceInfo) {
        binding?.image?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)?.asGifModel()

        val option = if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)
        } else {
            return
        }

        option?.setLoopCount(1)
//            ?.setAnimationCallback(object : AnimationCallback<CsGifDrawable>{
//                override fun onAnimationEnd(drawable: CsGifDrawable) {
//                    CsLogger.i("onAnimationEnd. drawable=$drawable")
//                }
//
//                override fun onAnimationStart(drawable: CsGifDrawable) {
//                    CsLogger.i("onAnimationStart. drawable=$drawable")
//                }
//            })
            ?.into(binding?.image)
    }

    private fun loadPng1(info: ResourceInfo) {
        binding?.image?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)

        if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)?.into(binding?.image)
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)?.into(binding?.image)
        }
    }

    private fun loadWebp1(info: ResourceInfo) {
        binding?.image?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)

        if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)?.into(binding?.image)
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)?.into(binding?.image)
        }
    }

    private fun loadWebp2(info: ResourceInfo) {
        binding?.image?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)?.asWebpModel()

        val option = if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)
        } else {
            return
        }

        option?.setLoopCount(1)
//            ?.setAnimationCallback(object :AnimationCallback<CsWebpDrawable>{
//                override fun onAnimationEnd(drawable: CsWebpDrawable) {
//                    CsLogger.i("onAnimationEnd. drawable=$drawable")
//                }
//
//                override fun onAnimationStart(drawable: CsWebpDrawable) {
//                    CsLogger.i("onAnimationStart. drawable=$drawable")
//                }
//            })
            ?.into(binding?.image)
    }

    private fun loadLottie(info: ResourceInfo) {
        binding?.layout?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)?.asLottieModel()

        val option = if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)
//            loader?.loadAsset("image/motivate_3.1_lottie.zip")
//            loader?.loadPath("/data/data/com.proxy.service.funsdk/cache/lottie_network_cache/lottie_cache_httpsreadbookstatictestosscnbeijingaliyuncscombizuploads20240507E99885E8AFBBE69CACtest_lottiemotivate_31_lottiezip.zip")
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)
        } else {
            return
        }

        option
            ?.setLoopCount(1)
//            ?.setLoopModel(LottieLoopModel.REVERSE)
            ?.setLoadErrorCallback(object : LoadErrorCallback {
                override fun onLoadError() {
                    CsLogger.tag("qqqqqqq").e("lottie load error")
                }
            })
            ?.into(binding?.layout)
    }

    private fun loadPag(info: ResourceInfo) {
        binding?.layout?.visibility = View.VISIBLE
        val loader = CsImageLoader.with(this)?.asPagModel()

        val option = if (binding?.isFromNetWork?.isChecked == true) {
            loader?.loadUrl(info.url)?.replaceText(
                0,
                PagTextData.builder("21")
                    .setPagTextFont(
                        PagTextFont.builderWithUrl("https://readbook-static-test.oss-cn-beijing.aliyuncs.com/biz/uploads/20240507/%E9%98%85%E8%AF%BB%E6%9C%AC/%E5%9B%BE%E7%89%87/SourceHanSansCNBold.otf")
                            .build()
                    ).build()
            )?.replaceImage(
                0,
                PagImageData.builderWithPath("/storage/emulated/0/Android/data/com.proxy.service.funsdk/files/jpg.jpg")
//                PagImageData.builderWithPath("")
//                PagImageData.builderWithPath("/storage/emulated/0/Android/data/com.proxy.service.funsdk/files/png.png")
//                PagImageData.builderWithRes(0)
                    .setScaleMode(PagScaleMode.CENTER_INSIDE)
                    .build()
            )
        } else if (binding?.isFromRes?.isChecked == true) {
            loader?.loadRes(info.res)?.replaceText(
                0,
                PagTextData.builder("31")
                    .setPagTextFont(
                        PagTextFont.builderWithPath("/sdcard/Android/data/com.proxy.service.funsdk/files/SourceHanSansCNBold.otf")
                            .build()
                    ).build()
            )
        } else {
            return
        }

        option
//            ?.setPagSceneMode(PagSceneMode.PERFORMANCE)
//            ?.setAnimationProgress(0.5)
//            ?.setAutoPlay(false)
            ?.setLoadErrorCallback(object : LoadErrorCallback {
                override fun onLoadError() {
                    CsLogger.tag("qqqqqqq").e("pag load error")
                }
            })?.into(binding?.layout)
    }

    private data class ResourceInfo(
        val url: String,
        val res: Int
    )
}