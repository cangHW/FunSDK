package com.proxy.service.funsdk.document.pdf.preview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.pdf.base.PdfService
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.config.info.FailedResult
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.document.pdf.base.enums.ViewShowType
import com.proxy.service.document.pdf.base.view.IPdfView
import com.proxy.service.document.pdf.base.view.callback.OnShouldCreateCoverViewCallback
import com.proxy.service.funsdk.R
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2025/5/3 12:02
 * @desc:
 */
class PdfPreViewActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, PdfPreViewActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var pdfView: IPdfView? = null

    private val type_1 = ViewShowType.SINGLE_PAGE_VERTICAL
    private val type_2 = ViewShowType.TWO_PAGE_TO_ONE_LANDSCAPE
    private var currentType = type_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_pdf_preview)

        pdfView = showPdf()

        findViewById<View>(R.id.click).setOnClickListener {
            currentType = if (currentType == type_1) {
                type_2
            } else {
                type_1
            }
            pdfView?.changeShowType(currentType)
        }
    }

    private fun showPdf(): IPdfView? {
        val group = findViewById<FrameLayout>(R.id.group)
        val service = CloudSystem.getService(PdfService::class.java)

        val config = PdfConfig.builder()
            .setSourceAssetPath("pdf/asd.pdf")
            .build()

        return service?.createViewFactory(config)
//            ?.setViewBackgroundColor(0xffff0000)
//            ?.setPageBackgroundColor(0xff00ff00)
            ?.setLifecycleOwner(this)
            ?.setShowType(currentType)
            ?.setLoadStateCallback(object : LoadStateCallback {
                override fun onLoadComplete(
                    success: List<BaseSource>,
                    failed: List<FailedResult>
                ) {
                    CsToast.show("PDF 加载完成，success=${success} failed=${failed}")
                    CsLogger.i("PDF 加载完成，success=${success} failed=${failed}")
                }
            })
            ?.into(group)
    }

}