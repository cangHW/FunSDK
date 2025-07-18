package com.proxy.service.funsdk.document.pdf

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.document.CsDocumentImage
import com.proxy.service.document.pdf.base.PdfService
import com.proxy.service.document.pdf.base.bean.CatalogueData
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.config.info.FailedResult
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2025/5/3 12:02
 * @desc:
 */
class PdfActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, PdfActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_pdf)

        val group = findViewById<FrameLayout>(R.id.group)

        val service = CloudSystem.getService(PdfService::class.java)

        val config = PdfConfig.builder()
            .setSourceAssetPath("pdf/asd.pdf")
            .build()

        service?.createViewFactory(config)
            ?.setPageBackgroundColor(0xffff0000)
            ?.setPageBackgroundColor(0xff00ff00)
            ?.setLifecycleOwner(this)
            ?.setLoadStateCallback(object : LoadStateCallback {
                override fun onLoadComplete(
                    success: List<BaseSource>,
                    failed: List<FailedResult>
                ) {
                    CsLogger.i("PDF 加载完成，success=${success} failed=${failed}")
                }
            })
            ?.into(group)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.log_common_msg -> {
//                CsLogger.i("PageCount = ${loader?.getPageCount()}")
//                CsLogger.i("DocumentMeta = ${loader?.getDocumentMeta()}")
//                loader?.getDocumentCatalogue()?.forEach {
//                    logCatalogue(it, "")
//                }
            }

        }
    }

    private fun logCatalogue(catalogue: CatalogueData, start: String) {
        CsLogger.i("${start}title=${catalogue.title}, pageIdx=${catalogue.pageIndex}, mNativePtr=${catalogue.hand}")
        catalogue.children.forEach {
            logCatalogue(it, "   $start")
        }
    }

}