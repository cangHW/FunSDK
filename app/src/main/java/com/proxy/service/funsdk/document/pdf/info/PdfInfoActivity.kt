package com.proxy.service.funsdk.document.pdf.info

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.document.CsDocumentPdf
import com.proxy.service.document.pdf.base.bean.CatalogueData
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.config.info.FailedResult
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.funsdk.R
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2025/5/3 12:02
 * @desc:
 */
class PdfInfoActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, PdfInfoActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var loader: IPdfLoader?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_pdf_info)

        val config = PdfConfig.builder()
            .setSourceAssetPath("pdf/asd.pdf")
            .build()

        loader = CsDocumentPdf.createLoader(config, object :LoadStateCallback{
            override fun onLoadComplete(success: List<BaseSource>, failed: List<FailedResult>) {
                CsToast.show("PDF 加载完成，success=${success} failed=${failed}")
                CsLogger.i("PDF 加载完成，success=${success} failed=${failed}")
            }
        })
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

        }
    }

    private fun logCatalogue(catalogue: CatalogueData, start: String) {
        CsLogger.i("${start}title=${catalogue.title}, pageIdx=${catalogue.pageIndex}, mNativePtr=${catalogue.hand}")
        catalogue.children.forEach {
            logCatalogue(it, "   $start")
        }
    }

}