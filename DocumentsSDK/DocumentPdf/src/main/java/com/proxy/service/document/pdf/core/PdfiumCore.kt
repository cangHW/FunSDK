package com.proxy.service.document.pdf.core

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.RectF
import android.view.Surface
import com.proxy.service.document.base.pdf.info.CatalogueData
import com.proxy.service.document.base.pdf.info.LinkData
import com.proxy.service.document.base.pdf.info.MetaData
import com.proxy.service.document.base.pdf.info.PageSize
import com.proxy.service.document.pdf.constants.Constants

/**
 * @author: cangHX
 * @data: 2025/5/2 08:19
 * @desc:
 */
class PdfiumCore {

    companion object {
        init {
            System.loadLibrary("c++_shared");
            System.loadLibrary("modpng");
            System.loadLibrary("modft2");
            System.loadLibrary("modpdfium");
            System.loadLibrary("csPdf");
        }

        private val core by lazy {
            PdfiumCore()
        }

        fun getInstance(): PdfiumCore {
            return core
        }
    }


    /*** *** *** *** *** *** *** 文档加载 *** *** *** *** *** *** ***/

    /**
     * 加载文档
     *
     * @param filePath  文件路径
     * @param password  密码
     * */
    external fun nativeOpenDocumentByPath(filePath: String, password: String?): OpenResult

    /**
     * 加载文档
     *
     * @param fd        文件描述符
     * @param password  密码
     * */
    external fun nativeOpenDocumentByFd(fd: Int, password: String?): OpenResult

    /**
     * 加载文档
     *
     * @param data      文件内容
     * @param password  密码
     * */
    external fun nativeOpenDocumentByMem(data: ByteArray, password: String?): OpenResult

    /**
     * 释放对应文档
     *
     * @param doc_hand 文档指针
     */
    external fun nativeCloseDocument(doc_hand: Long)


    /*** *** *** *** *** *** *** 文档基础信息 *** *** *** *** *** *** ***/

    /**
     * 获取对应文档内的页面数量
     *
     * @param doc_hand 文档指针
     */
    external fun nativeGetPageCount(doc_hand: Long): Int

    /**
     * 获取对应文档说明信息
     *
     * @param doc_hand 文档指针
     * @param tag      标签
     */
    private external fun nativeGetDocumentMetaText(doc_hand: Long, tag: String): String


    /*** *** *** *** *** *** *** 文档目录 *** *** *** *** *** *** ***/

    /**
     * 获取对应文档目录的第一个子目录指针
     *
     * @param doc_hand       文档指针
     * @param catalogue_hand 文档目录指针
     */
    private external fun nativeGetFirstChildBookmark(doc_hand: Long, catalogue_hand: Long?): Long?

    /**
     * 获取对应文档目录的下一个目录指针
     *
     * @param doc_hand       文档指针
     * @param catalogue_hand 文档目录指针
     */
    private external fun nativeGetNextChildBookmark(doc_hand: Long, catalogue_hand: Long): Long?

    /**
     * 获取对应文档目录标题
     *
     * @param catalogue_hand 文档目录指针
     */
    private external fun nativeGetBookmarkTitle(catalogue_hand: Long): String?

    /**
     * 获取文档目录对应的页码
     *
     * @param doc_hand       文档指针
     * @param catalogue_hand 文档目录指针
     */
    private external fun nativeGetBookmarkDestIndex(doc_hand: Long, catalogue_hand: Long): Long


    /*** *** *** *** *** *** *** 文档页面加载 *** *** *** *** *** *** ***/

    /**
     * 加载对应文档页面, 并获取页面指针
     *
     * @param doc_hand  文档指针
     * @param pageIndex 页面位置
     */
    external fun nativeLoadPage(doc_hand: Long, pageIndex: Int): Long

    /**
     * 批量加载对应文档页面, 并获取页面指针
     *
     * @param doc_hand  文档指针
     * @param fromIndex 页面开始位置
     * @param toIndex   页面结束位置
     */
    external fun nativeLoadPages(doc_hand: Long, fromIndex: Int, toIndex: Int): LongArray

    /**
     * 关闭文档对应页面
     *
     * @param page_hand 页面指针
     */
    external fun nativeClosePage(page_hand: Long)

    /**
     * 批量关闭文档对应页面
     *
     * @param pages_hands 页面指针
     */
    external fun nativeClosePages(pages_hands: LongArray)


    /*** *** *** *** *** *** *** 页面尺寸信息 *** *** *** *** *** *** ***/

    /**
     * 获取文档对应页面宽度, 单位: 像素
     *
     * @param page_hand 页面指针
     * @param dpi       像素密度
     * */
    external fun nativeGetPageWidthPixel(page_hand: Long, dpi: Int): Int

    /**
     * 获取文档对应页面高度, 单位: 像素
     *
     * @param page_hand 页面指针
     * @param dpi       像素密度
     * */
    external fun nativeGetPageHeightPixel(page_hand: Long, dpi: Int): Int

    /**
     * 获取文档对应页面宽度, 单位: 文档点
     *
     * @param page_hand 页面指针
     * */
    external fun nativeGetPageWidthPoint(page_hand: Long): Int

    /**
     * 获取文档对应页面高度, 单位: 文档点
     *
     * @param page_hand 页面指针
     * */
    external fun nativeGetPageHeightPoint(page_hand: Long): Int

    /**
     * 通过页码获取对应文档页面尺寸, 单位: 像素
     *
     * @param doc_hand  文档指针
     * @param pageIndex 文档页码
     * @param dpi       像素密度
     * */
    private external fun nativeGetPageSizeByIndex(
        doc_hand: Long,
        pageIndex: Int,
        dpi: Int
    ): PageSize


    /*** *** *** *** *** *** *** 页面超链 *** *** *** *** *** *** ***/

    /**
     * 获取文档对应页面的全部超链接信息
     *
     * @param page_hand 页面指针
     * */
    private external fun nativeGetPageLinks(page_hand: Long): LongArray

    /**
     * 获取超链接对应的目标页码
     *
     * @param doc_hand  页面指针
     * @param link_hand 超链接指针
     * */
    private external fun nativeGetDestPageIndex(doc_hand: Long, link_hand: Long): Int?

    /**
     * 获取超链接对应的目标 uri
     *
     * @param doc_hand  页面指针
     * @param link_hand 超链接指针
     * */
    private external fun nativeGetLinkURI(doc_hand: Long, link_hand: Long): String?

    /**
     * 获取超链接对应的坐标区域
     *
     * @param link_hand 超链接指针
     * */
    private external fun nativeGetLinkRectF(link_hand: Long): RectF?


    /*** *** *** *** *** *** *** 页面渲染-整体渲染 *** *** *** *** *** *** ***/

    /**
     * 渲染页面内容到 surface
     *
     * @param page_hand     页面指针
     * @param surface       surface
     * @param dpi           像素密度
     * @param startX        渲染区域的开始坐标, 相对于 bitmap 左上角的 x 轴位置
     * @param startY        渲染区域的开始坐标, 相对于 bitmap 左上角的 y 轴位置
     * @param width         渲染区域的宽度
     * @param height        渲染内容的高度
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    external fun nativeRenderPageToSurface(
        page_hand: Long,
        surface: Surface,
        dpi: Int,
        startX: Int,
        startY: Int,
        width: Int,
        height: Int,
        renderAnnot: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    )

    /**
     * 渲染页面内容到 bitmap
     *
     * @param page_hand     页面指针
     * @param bitmap        bitmap
     * @param dpi           像素密度
     * @param startX        渲染区域的开始坐标, 相对于 bitmap 左上角的 x 轴位置
     * @param startY        渲染区域的开始坐标, 相对于 bitmap 左上角的 y 轴位置
     * @param width         渲染区域的宽度
     * @param height        渲染内容的高度
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    external fun nativeRenderPageToBitmap(
        page_hand: Long,
        bitmap: Bitmap,
        dpi: Int,
        startX: Int,
        startY: Int,
        width: Int,
        height: Int,
        renderAnnot: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    )


    /*** *** *** *** *** *** *** 页面渲染-渐进式渲染 *** *** *** *** *** *** ***/


    /*** *** *** *** *** *** *** 通用能力 *** *** *** *** *** *** ***/

    /**
     * 页面坐标映射为屏幕坐标
     *
     * @param pagePtr 页面指针
     * @param startX  显示区域在设备坐标中的左侧像素位置
     * @param startY  显示区域在设备坐标中的顶部像素位置
     * @param sizeX   显示区域的宽度(单位：像素)
     * @param sizeY   显示区域的高度(单位：像素)
     * @param rotate  页面方向：0（正常）, 1（顺时针旋转90度）, 2（顺时针旋转180度）, 3（顺时针旋转270度）
     * @param pageX   页面坐标中的X值
     * @param pageY   页面坐标中的Y值
     * @return 返回映射坐标
     */
    private external fun nativePageCoordsToDevice(
        pagePtr: Long,
        startX: Int,
        startY: Int,
        sizeX: Int,
        sizeY: Int,
        rotate: Int,
        pageX: Double,
        pageY: Double
    ): Point?


    /**************************************************/


    /**
     * 获取对应文档说明信息
     *
     * @param doc_hand 文档指针
     */
    fun getDocumentMeta(doc_hand: Long): MetaData {
        val meta = MetaData()
        meta.title = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_TITLE)
        meta.author = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_AUTHOR)
        meta.subject = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_SUBJECT)
        meta.keywords = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_KEYWORDS)
        meta.creator = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_CREATOR)
        meta.producer = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_PRODUCER)
        meta.creationDate =
            core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_CREATION_DATE)
        meta.modDate = core.nativeGetDocumentMetaText(doc_hand, Constants.DOC_META_MOD_DATE)
        return meta
    }

    /**
     * 获取对应文档目录信息
     *
     * @param doc_hand 文档指针
     */
    fun getDocumentCatalogue(doc_hand: Long): ArrayList<CatalogueData> {
        val list = ArrayList<CatalogueData>()
        val hand = core.nativeGetFirstChildBookmark(doc_hand, null)
        if (hand != null) {
            queryDocumentCatalogue(list, core, doc_hand, hand)
        }
        return list
    }

    private fun queryDocumentCatalogue(
        list: ArrayList<CatalogueData>,
        core: PdfiumCore,
        doc_hand: Long,
        catalogue_hand: Long
    ) {
        val catalogueData = CatalogueData()
        catalogueData.hand = catalogue_hand
        catalogueData.title = core.nativeGetBookmarkTitle(catalogue_hand)
        catalogueData.pageIndex = core.nativeGetBookmarkDestIndex(doc_hand, catalogue_hand)
        list.add(catalogueData)

        val child_catalogue_hand = core.nativeGetFirstChildBookmark(doc_hand, catalogue_hand)
        if (child_catalogue_hand != null) {
            queryDocumentCatalogue(
                catalogueData.children,
                core,
                doc_hand,
                child_catalogue_hand
            )
        }

        val next_catalogue_hand = core.nativeGetNextChildBookmark(doc_hand, catalogue_hand)
        if (next_catalogue_hand != null) {
            queryDocumentCatalogue(list, core, doc_hand, next_catalogue_hand)
        }
    }

    /**
     * 获取对应页面的超链接信息
     *
     * @param doc_hand  文档指针
     * @param page_hand 页面指针
     */
    fun getPageLinks(doc_hand: Long, page_hand: Long): ArrayList<LinkData> {
        val list = ArrayList<LinkData>()
        val link_hands = nativeGetPageLinks(page_hand)

        link_hands.forEach {
            val rectf = nativeGetLinkRectF(it) ?: return@forEach

            val index = nativeGetDestPageIndex(doc_hand, it)
            val uri = nativeGetLinkURI(doc_hand, it)

            if (index != null || uri != null) {
                return@forEach
            }

            list.add(LinkData(rectf, index, uri))
        }
        return list
    }
}