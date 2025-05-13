package com.proxy.service.funsdk.document.pdf

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.base.PdfService
import com.proxy.service.document.base.config.pdf.PdfConfig
import com.proxy.service.document.base.config.pdf.callback.LoadStateCallback
import com.proxy.service.document.base.config.pdf.info.FailedResult
import com.proxy.service.document.base.config.pdf.source.BaseSource
import com.proxy.service.document.base.pdf.IPdfLoader
import com.proxy.service.document.base.pdf.info.CatalogueData
import com.proxy.service.document.pdf.view.TempSurfaceView
import com.proxy.service.document.pdf.view.TempView
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

    private var loader: IPdfLoader? = null

    private var tempSurfaceView: TempSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_pdf)

//        val tempView = findViewById<TempView>(R.id.temp)
        tempSurfaceView = findViewById(R.id.temp_surface)

        val service = CloudSystem.getService(PdfService::class.java)
        loader = service?.createLoader(
            PdfConfig.builder()
                .setSourceAssetPath("pdf/asd.pdf")
                .setLoadCallback(object : LoadStateCallback {
                    override fun onLoadComplete(
                        success: List<BaseSource>,
                        failed: List<FailedResult>
                    ) {
                        CsLogger.i("PDF 加载完成，success=${success} failed=${failed}")
//                        tempView?.setLoader(loader)
                        tempSurfaceView?.setLoader(loader)
                    }
                }).build()
        )
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.log_common_msg -> {
                CsLogger.i("PageCount = ${loader?.getPageCount()}")
                CsLogger.i("DocumentMeta = ${loader?.getDocumentMeta()}")
                loader?.getDocumentCatalogue()?.forEach {
                    logCatalogue(it, "")
                }
            }

            R.id.last_page -> {
                tempSurfaceView?.let {
                    it.showContent(it.getShowIndex() - 1)
                }
            }

            R.id.next_page -> {
                tempSurfaceView?.let {
                    it.showContent(it.getShowIndex() + 1)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loader?.destroy()
    }

    private fun logCatalogue(catalogue: CatalogueData, start: String) {
        CsLogger.i("${start}title=${catalogue.title}, pageIdx=${catalogue.pageIndex}, mNativePtr=${catalogue.hand}")
        catalogue.children.forEach {
            logCatalogue(it, "   $start")
        }
    }

}