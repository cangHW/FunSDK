package com.proxy.service.camera.base.mode.loader

/**
 * @author: cangHX
 * @data: 2026/4/21 10:40
 * @desc:
 */
enum class VideoRecordState {

    /**
     * 空闲
     * */
    STATE_IDLE,

    /**
     * 开始中
     * */
    STATE_STARTING,

    /**
     * 录制中
     * */
    STATE_RECORDING,

    /**
     * 关闭中
     * */
    STATE_STOPPING;

}