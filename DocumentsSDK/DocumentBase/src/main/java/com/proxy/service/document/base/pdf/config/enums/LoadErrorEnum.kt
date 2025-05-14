package com.proxy.service.document.base.pdf.config.enums

/**
 * @author: cangHX
 * @data: 2025/5/5 15:04
 * @desc:
 */
enum class LoadErrorEnum(val errorCode: Long) {

    /**
     * 未知异常
     * */
    UNKNOWN(1),

    /**
     * 文件未找到或无法打开
     * */
    FILE_ERROR(2),

    /**
     * 文件不是PDF格式或已损坏
     * */
    FORMAT_ERROR(3),

    /**
     * 未输入密码或密码错误
     * */
    PASSWORD_ERROR(4),

    /**
     * 安全问题
     * */
    SECURITY_ERROR(5),

    /**
     * 未找到页面或内容错误
     * */
    PAGE_ERROR(6);


    companion object {
        fun valueOf(errorCode: Long): LoadErrorEnum? {
            when (errorCode) {
                UNKNOWN.errorCode -> {
                    return UNKNOWN
                }

                FILE_ERROR.errorCode -> {
                    return FILE_ERROR
                }

                FORMAT_ERROR.errorCode -> {
                    return FORMAT_ERROR
                }

                PASSWORD_ERROR.errorCode -> {
                    return PASSWORD_ERROR
                }

                SECURITY_ERROR.errorCode -> {
                    return SECURITY_ERROR
                }

                PAGE_ERROR.errorCode -> {
                    return PAGE_ERROR
                }
            }
            return UNKNOWN
        }
    }

}