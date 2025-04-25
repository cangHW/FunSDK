package com.proxy.service.core.framework.data.time.enums

/**
 * @author: cangHX
 * @data: 2025/4/14 11:24
 * @desc:
 */
enum class TimeFormat(val pattern: String) {

    /**
     * 年-月-日
     *
     * 示例: 2023-10-05
     * */
    TYPE_Y_M_D_1("yyyy-MM-dd"),

    /**
     * 年/月/日
     *
     * 示例: 2023/10/05
     * */
    TYPE_Y_M_D_2("yyyy/MM/dd"),

    /**
     * 年-月-日 时:分:秒
     *
     * 示例: 2023-10-05 14:30:00
     * */
    TYPE_Y_M_D_H_M_S_1("yyyy-MM-dd HH:mm:ss"),

    /**
     * 年-月-日 时:分:秒.毫秒
     *
     * 示例: 2023-10-05 14:30:00.231
     * */
    TYPE_Y_M_D_H_M_S_S("yyyy-MM-dd HH:mm:ss.SSS"),

    /**
     * 年-月-日 时:分:秒 时区
     *
     * 示例: 2023-10-05 14:30:00 +0000
     * */
    TYPE_Y_M_D_H_M_S_Z_1("yyyy-MM-dd HH:mm:ss Z"),

    /**
     * 年-月-日 时:分:秒 时区
     *
     * 示例: 2023-10-05 14:30:00 UTC
     * */
    TYPE_Y_M_D_H_M_S_Z_2("yyyy-MM-dd HH:mm:ss z"),

    /**
     * 年/月/日 时:分:秒
     *
     * 示例: 2023/10/05 14:30:00
     * */
    TYPE_Y_M_D_H_M_S_2("yyyy/MM/dd HH:mm:ss"),

    /**
     * 月-日-年
     *
     * 示例: 10/05/2023
     * */
    TYPE_M_D_Y_1("MM-dd-yyyy"),

    /**
     * 月-日-年 时:分:秒
     *
     * 示例: 10/05/2023 14:30:00
     * */
    TYPE_M_D_Y_H_M_S("MM-dd-yyyy HH:mm:ss"),

    /**
     * 月/日/年
     *
     * 示例: 10/05/2023
     * */
    TYPE_M_D_Y_2("MM/dd/yyyy"),

    /**
     * 日-月-年
     *
     * 示例: 05-10-2023
     * */
    TYPE_D_M_Y("dd-MM-yyyy"),

    /**
     * 日-月-年 时:分:秒
     *
     * 示例: 05-10-2023 14:30:00
     * */
    TYPE_D_M_Y_H_M_S("dd-MM-yyyy HH:mm:ss"),

    /**
     * 时:分
     *
     * 示例: 14:30
     * */
    TYPE_H_M("HH:mm"),

    /**
     * 时:分:秒
     *
     * 示例: 14:30:00
     * */
    TYPE_H_M_S("HH:mm:ss"),

    /**
     * 时:分:秒.毫秒
     *
     * 示例: 14:30:00.231
     * */
    TYPE_H_M_S_S("HH:mm:ss.SSS");
}