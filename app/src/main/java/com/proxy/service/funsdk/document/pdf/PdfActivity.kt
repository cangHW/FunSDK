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
import com.proxy.service.document.base.PdfService
import com.proxy.service.document.base.pdf.config.PdfConfig
import com.proxy.service.document.base.pdf.config.callback.LoadStateCallback
import com.proxy.service.document.base.pdf.config.info.FailedResult
import com.proxy.service.document.base.pdf.config.source.BaseSource
import com.proxy.service.document.base.pdf.loader.IPdfLoader
import com.proxy.service.document.base.pdf.info.CatalogueData
import com.proxy.service.document.pdf.view.view.TempSurfaceView
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
            .setLoadCallback(object : LoadStateCallback {
                override fun onLoadComplete(
                    success: List<BaseSource>,
                    failed: List<FailedResult>
                ) {
                    CsLogger.i("PDF 加载完成，success=${success} failed=${failed}")
                }
            }).build()

        service?.createViewFactory(config)
            ?.setLifecycleOwner(this)
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