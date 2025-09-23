package com.proxy.service.core.framework.data.time.enums

/**
 * @author: cangHX
 * @data: 2025/4/14 11:24
 * @desc: F- 代表不确定性规则
 * F- 会把为 0 的值移除
 * F-+ 会把为 0 的值移除，如果最终值为空，则保留最后一个为 0 的值
 *
 * DD 天
 * HH、H 小时
 * MM、M 分钟
 * SS、S 秒钟
 * CCC 毫秒
 */
enum class TimeIntervalFormat(val pattern: String) {

    /**
     * 日天时小时分分钟秒秒毫秒毫秒
     *
     * 示例: 10天14小时30分钟00秒钟231毫秒
     * */
    TYPE_D_HH_MM_SS_MS("DD天HH小时MM分钟SS秒CCC毫秒"),

    /**
     * 日天时小时分分钟秒秒毫秒毫秒
     *
     * 示例: 10天14小时30分钟0秒钟231毫秒
     * */
    TYPE_D_H_M_S_MS("DD天H小时M分钟S秒CCC毫秒"),

    /**
     * 时:分
     *
     * 示例: 14:30
     * */
    TYPE_HH_MM("HH:MM"),

    /**
     * 时:分
     *
     * 示例: 14:30
     * */
    TYPE_H_M("H:M"),

    /**
     * 时:分:秒
     *
     * 示例: 14:30:00
     * */
    TYPE_HH_MM_SS("HH:MM:SS"),

    /**
     * 时:分:秒
     *
     * 示例: 14:30:0
     * */
    TYPE_H_M_S("H:M:S"),

    /**
     * 时:分:秒.毫秒
     *
     * 示例: 14:30:00.231
     * */
    TYPE_HH_MM_SS_MS("HH:MM:SS.CCC"),

    /**
     * 时:分:秒.毫秒
     *
     * 示例: 14:30:0.231
     * */
    TYPE_H_M_S_MS("H:M:S.CCC"),

    /**
     * 分分钟秒秒毫秒毫秒
     *
     * 示例: 30分钟00秒钟231毫秒
     * */
    TYPE_MM_SS_MS("MM分钟SS秒CCC毫秒"),

    /**
     * 分分钟秒秒毫秒毫秒
     *
     * 示例: 30分钟0秒钟231毫秒
     * */
    TYPE_M_S_MS("M分钟S秒CCC毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_D_HH_MM_SS_MS_1("F-DD天HH小时MM分钟SS秒CCC毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_D_H_M_S_MS_1("F-DD天H小时M分钟S秒CCC毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_D_HH_MM_SS_MS_2("F-DDdHHhMMmSSsCCCms"),

    /**
     * 不确定性格式
     * */
    TYPE_F_D_H_M_S_MS_2("F-DDdHhMmSsCCCms"),

    /**
     * 不确定性格式
     * */
    TYPE_F_1_D_HH_MM_SS_MS_1("F-+DD天HH小时MM分钟SS秒CCC毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_1_D_H_M_S_MS_1("F-+DD天H小时M分钟S秒CCC毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_1_D_HH_MM_SS_MS_2("F-+DDdHHhMMmSSsCCCms"),

    /**
     * 不确定性格式
     * */
    TYPE_F_1_D_H_M_S_MS_2("F-+DDdHhMmSsCCCms");
}