package com.proxy.service.webview.base.listener

/**
 * @author: cangHX
 * @data: 2026/1/5 09:57
 * @desc:
 */
interface WebDownloadListener {

    /**
     * 触发下载
     *
     * @param url                   下载链接
     * @param fileName              基于下载信息推测的文件名称
     * @param userAgent             用户代理字符串
     * @param contentDisposition    内容描述头，通常包含文件名信息
     * @param mimetype              文件的 MIME 类型 (如 application/pdf、image/png 等)
     * @param contentLength         文件的大小 (以字节为单位)
     * */
    fun onDownloadStart(
        url: String,
        fileName: String?,
        userAgent: String,
        contentDisposition: String,
        mimetype: String,
        contentLength: Long
    )

}