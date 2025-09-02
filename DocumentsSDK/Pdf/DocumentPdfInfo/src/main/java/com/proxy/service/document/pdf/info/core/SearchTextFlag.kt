package com.proxy.service.document.pdf.info.core

/**
 * @author: cangHX
 * @data: 2025/8/28 16:57
 * @desc:
 */
enum class SearchTextFlag(val flag: Int) {

    /**
     * 默认, 不区分大小写、不匹配完整单词
     * */
    DEFAULT(0),

    /**
     * 区分大小写
     * */
    MATCH_CASE(1),

    /**
     * 匹配完整单词
     * */
    MATCH_WHOLE_WORD(2),

    /**
     * 区分大小写 + 匹配完整单词
     * */
    MATCH_CASE_AND_WHOLE_WORD(3);

}