package com.proxy.service.core.framework.system.share

import android.content.Intent
import android.net.Uri
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.uri.CsUriUtils

/**
 * 系统分享相关工具
 *
 * @author: cangHX
 * @data: 2024/10/30 10:06
 * @desc:
 */
object CsSystemShareUtils {

    private const val TAG = "${Constants.TAG}SystemShare"

    /**
     * 分享文字
     *
     * @param title 分享的标题
     * @param info  分享文字内容
     * */
    fun shareTxt(title: String?, info: String?) {
        CsLogger.tag(TAG).d("shareTxt, title = $title, info = $info")

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, info ?: "")

        val choose = Intent.createChooser(intent, title ?: "")
        choose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            CsContextManager.getApplication().startActivity(choose)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    /**
     * 分享图片
     *
     * @param title     分享的标题
     * @param imgPaths  图片地址集合
     */
    fun shareImg(title: String?, imgPaths: List<String>?) {
        CsLogger.tag(TAG).d("shareImg, title = $title, imgPaths = ${imgPaths?.joinToString()}")
        if (imgPaths.isNullOrEmpty()) {
            return
        }

        val imageUris = ArrayList<Uri>()
        imgPaths.forEach {
            CsUriUtils.getUriByPath(it)?.let { uri ->
                imageUris.add(uri)
            }
        }

        if (imageUris.isEmpty()) {
            return
        }

        val intent = Intent()

        if (imageUris.size == 1) {
            intent.setAction(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, imageUris[0])
        } else {
            intent.setAction(Intent.ACTION_SEND_MULTIPLE)
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setType("image/*");

        val choose = Intent.createChooser(intent, title)
        choose.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            CsContextManager.getApplication().startActivity(choose)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

}