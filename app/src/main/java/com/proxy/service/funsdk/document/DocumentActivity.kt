package com.proxy.service.funsdk.document

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.document.image.ImageActivity
import com.proxy.service.funsdk.document.pdf.PdfActivity

/**
 * @author: cangHX
 * @data: 2025/5/3 12:03
 * @desc:
 */
class DocumentActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.pdf -> {
                PdfActivity.launch(this)
            }

            R.id.image -> {
                ImageActivity.launch(this)
            }
        }
    }
}