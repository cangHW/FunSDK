package com.proxy.service.core.framework.system.sound.enums

import android.media.AudioAttributes

/**
 * @author: cangHX
 * @data: 2024/12/5 20:34
 * @desc:
 */
enum class ContentTypeEnum(usage: Int) {

    /**
     * 未知用法
     */
     USAGE_UNKNOWN(AudioAttributes.USAGE_UNKNOWN),

    /**
     * 媒体播放
     */
    var USAGE_MEDIA: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 通话
     */
    var USAGE_VOICE_COMMUNICATION: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 呼叫铃声
     */
    var USAGE_VOICE_COMMUNICATION_SIGNALLING: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 闹钟
     */
    var USAGE_ALARM: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 通知
     */
    var USAGE_NOTIFICATION: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 电话铃声
     */
    var USAGE_NOTIFICATION_RINGTONE: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 用于输入/结束通信请求，如VoIP通信、视频会议等。
     */
    var USAGE_NOTIFICATION_COMMUNICATION_REQUEST: com.proxy.service.api.enums.CloudUsageEnum? =
        null,

    /**
     * 即时通信的通知(如聊天或短信)
     */
    var USAGE_NOTIFICATION_COMMUNICATION_INSTANT: com.proxy.service.api.enums.CloudUsageEnum? =
        null,

    /**
     * 非即时通信的通知(如电子邮件)
     */
    var USAGE_NOTIFICATION_COMMUNICATION_DELAYED: com.proxy.service.api.enums.CloudUsageEnum? =
        null,

    /**
     * 用于吸引用户的注意，如提示或低电量警告。
     */
    var USAGE_NOTIFICATION_EVENT: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 用于可访问性时(如使用屏幕阅读器)
     */
    var USAGE_ASSISTANCE_ACCESSIBILITY: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 驾驶或导航
     */
    var USAGE_ASSISTANCE_NAVIGATION_GUIDANCE: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 声音化时(如用户界面声音)
     */
    var USAGE_ASSISTANCE_SONIFICATION: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 游戏音频
     */
    var USAGE_GAME: com.proxy.service.api.enums.CloudUsageEnum? = null,

    /**
     * 用于音频回应用户查询，音频说明或帮助话语。
     */
    var USAGE_ASSISTANT: com.proxy.service.api.enums.CloudUsageEnum? = null;

}