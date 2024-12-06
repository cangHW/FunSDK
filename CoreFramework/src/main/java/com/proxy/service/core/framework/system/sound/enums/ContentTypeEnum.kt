package com.proxy.service.core.framework.system.sound.enums

import android.media.AudioAttributes

/**
 * @author: cangHX
 * @data: 2024/12/5 20:40
 * @desc:
 */
enum class ContentTypeEnum(val contentType: Int) {

    /**
     * 类型未知
     */
    CONTENT_TYPE_UNKNOWN(AudioAttributes.CONTENT_TYPE_UNKNOWN),

    /**
     * 语音
     */
    CONTENT_TYPE_SPEECH(AudioAttributes.CONTENT_TYPE_SPEECH),

    /**
     * 单纯音频，如：音乐
     */
    CONTENT_TYPE_MUSIC(AudioAttributes.CONTENT_TYPE_MUSIC),

    /**
     * 音频，如：视频中的音频
     */
    CONTENT_TYPE_MOVIE(AudioAttributes.CONTENT_TYPE_MOVIE),

    /**
     * 交互音频，如：按键反馈、游戏奖励发放
     */
    CONTENT_TYPE_SONIFICATION(AudioAttributes.CONTENT_TYPE_SONIFICATION);

}