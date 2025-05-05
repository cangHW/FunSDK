package com.proxy.service.document.pdf.core

import com.proxy.service.document.base.pdf.info.CatalogueData

/**
 * @author: cangHX
 * @data: 2025/5/2 08:51
 * @desc:
 */
object CoreUtils {

    /**
     * 查询文档目录
     * */
    fun queryDocumentCatalogue(
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
            queryDocumentCatalogue(catalogueData.children, core, doc_hand, child_catalogue_hand)
        }

        val next_catalogue_hand = core.nativeGetSiblingBookmark(doc_hand, catalogue_hand)
        if (next_catalogue_hand != null) {
            queryDocumentCatalogue(list, core, doc_hand, next_catalogue_hand)
        }
    }

}