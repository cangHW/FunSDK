package com.proxy.service.core.framework.data.time.enums

/**
 * @author: cangHX
 * @data: 2025/4/14 11:24
 * @desc: F- 代表不确定性规则
 * F- 会把为 0 的值移除
 * F-+ 会把为 0 的值移除，如果最终值为空，则保留最后一个为 0 的值
 */
enum class TimeIntervalFormat(val pattern: String) {

    /**
     * 日天时小时分分钟秒秒毫秒毫秒
     *
     * 示例: 10天14小时30分钟0秒钟231毫秒
     * */
    TYPE_D_H_M_S_MS("dd天HH小时mm分钟ss秒SSS毫秒"),

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
    TYPE_H_M_S_MS("HH:mm:ss.SSS"),

    /**
     * 分分钟秒秒毫秒毫秒
     *
     * 示例: 30分钟0秒钟231毫秒
     * */
    TYPE_M_S_MS("mm分钟ss秒SSS毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_D_H_M_S_MS_1("F-dd天HH小时mm分钟ss秒SSS毫秒"),

    /**
     * 不确定性格式
     * */
    TYPE_F_D_H_M_S_MS_2("F-dddHHhmmmsssSSSms"),

    /**
     * 不确定性格式
     * */
    TYPE_F_1_D_H_M_S_MS_2("F-+dddHHhmmmsssSSSms");
}