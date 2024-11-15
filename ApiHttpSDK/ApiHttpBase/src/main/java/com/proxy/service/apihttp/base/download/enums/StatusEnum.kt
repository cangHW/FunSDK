package com.proxy.service.apihttp.base.download.enums

/**
 * @author: cangHX
 * @data: 2024/11/7 14:39
 * @desc:
 */
enum class StatusEnum(val status: Int) {

    /**
     * 未知, 当前任务未记录过
     * */
    UNKNOWN(-1),

    /**
     * 已记录, 未添加
     * */
    RECORDED(1),

    /**
     * 已添加, 等待开始任务
     * */
    WAITING(2),

    /**
     * 开始
     * */
    START(3),

    /**
     * 下载中
     * */
    PROGRESS(4),

    /**
     * 取消
     * */
    CANCEL(5),

    /**
     * 成功
     * */
    SUCCESS(6),

    /**
     * 失败
     * */
    FAILED(7),

    /**
     * 文件被移除
     * */
    FILE_DELETE(-2);

    companion object {
        fun value(status: Int): StatusEnum {
            when (status) {
                WAITING.status -> {
                    return WAITING
                }

                START.status -> {
                    return START
                }

                PROGRESS.status -> {
                    return PROGRESS
                }

                CANCEL.status -> {
                    return CANCEL
                }

                SUCCESS.status -> {
                    return SUCCESS
                }

                FAILED.status -> {
                    return FAILED
                }
            }
            return UNKNOWN
        }
    }

}