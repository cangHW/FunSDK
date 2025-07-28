package com.proxy.service.funsdk.document.pdf

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.document.pdf.info.PdfInfoActivity
import com.proxy.service.funsdk.document.pdf.preview.PdfPreViewActivity

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
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.info -> {
                PdfInfoActivity.launch(this)
            }
            R.id.preview -> {
                PdfPreViewActivity.launch(this)
            }
            R.id.edit -> {
//                PdfInfoActivity.launch(this)
            }
        }
    }

}