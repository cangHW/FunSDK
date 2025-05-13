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
            System.loadLibrary("jniPdfium");
        }

        private val core by lazy {
            PdfiumCore()
        }

        fun getInstance(): PdfiumCore {
            return core
        }
    }

    /**
     * 打开文档
     *
     * @param filePath  文件路径
     * @param password  密码
     * */
    external fun nativeOpenDocumentByPath(filePath: String, password: String?): OpenResult

    /**
     * 打开文档
     *
     * @param fd        文件描述符
     * @param password  密码
     * */
    external fun nativeOpenDocumentByFd(fd: Int, password: String?): OpenResult

    /**
     * 打开文档
     *
     * @param data      文件内容
     * @param password  密码
     * */
    external fun nativeOpenDocumentByMem(data: ByteArray, password: String?): OpenResult

    /**
     * 关闭对应文档
     *
     * @param doc_hand 文档指针
     */
    external fun nativeCloseDocument(doc_hand: Long)

    /**
     * 获取对应文档内的页面数量
     *
     * @param doc_hand 文档指针
     */
    external fun nativeGetPageCount(doc_hand: Long): Int

    /**
     * 加载对应文档页面, 并获取页面指针
     *
     * @param doc_hand  文档指针
     * @param pageIndex 页面位置
     */
    external fun nativeLoadPage(doc_hand: Long, pageIndex: Int): Long

    private external fun nativeLoadPages(doc_hand: Long, fromIndex: Int, toIndex: Int): LongArray

    /**
     * 关闭文档对应页面
     *
     * @param page_hand 页面指针
     */
    external fun nativeClosePage(page_hand: Long)

    /**
     * 关闭文档对应页面
     *
     * @param pages_hands 页面指针
     */
    external fun nativeClosePages(pages_hands: LongArray)

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

    private external fun nativeRenderPage(
        pagePtr: Long,
        surface: Surface,
        dpi: Int,
        startX: Int,
        startY: Int,
        drawSizeHor: Int,
        drawSizeVer: Int,
        renderAnnot: Boolean
    )

    private external fun nativeRenderPageBitmap(
        pagePtr: Long,
        bitmap: Bitmap,
        dpi: Int,
        startX: Int,
        startY: Int,
        drawSizeHor: Int,
        drawSizeVer: Int,
        renderAnnot: Boolean
    )

    /**
     * 获取对应文档说明信息
     *
     * @param doc_hand 文档指针
     * @param tag      标签
     */
    private external fun nativeGetDocumentMetaText(doc_hand: Long, tag: String): String

    /**
     * 获取对应文档目录的第一个子目录指针
     *
     * @param doc_hand       文档指针
     * @param catalogue_hand 文档目录指针
     */
    external fun nativeGetFirstChildBookmark(doc_hand: Long, catalogue_hand: Long?): Long?

    /**
     * 获取对应文档目录的下一个目录指针
     *
     * @param doc_hand       文档指针
     * @param catalogue_hand 文档目录指针
     */
    external fun nativeGetSiblingBookmark(doc_hand: Long, catalogue_hand: Long): Long?

    /**
     * 获取对应文档目录标题
     *
     * @param catalogue_hand 文档目录指针
     */
    external fun nativeGetBookmarkTitle(catalogue_hand: Long): String?

    /**
     * 获取文档目录对应的页码
     *
     * @param doc_hand       文档指针
     * @param catalogue_hand 文档目录指针
     */
    external fun nativeGetBookmarkDestIndex(doc_hand: Long, catalogue_hand: Long): Long

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
            CoreUtils.queryDocumentCatalogue(list, core, doc_hand, hand)
        }
        return list
    }

    /**
     * 获取对应文档页面的超链接信息
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