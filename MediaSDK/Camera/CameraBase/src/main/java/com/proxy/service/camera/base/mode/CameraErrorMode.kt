package com.proxy.service.camera.base.mode

/**
 * @author: cangHX
 * @data: 2026/3/23 10:37
 * @desc:
 */
enum class CameraErrorMode {

    /**
     * 未知异常
     * */
    ERROR_UNKNOWN,

    /**
     * 摄像头设备已经被其他高优先级的客户端使用，导致当前打开摄像头失败
     * */
    ERROR_CAMERA_IN_USE,

    /**
     * 系统中打开的摄像头设备数量已经达到上限，无法再打开新的摄像头设备
     * */
    ERROR_MAX_CAMERAS_IN_USE,

    /**
     * 由于设备策略（例如企业设备管理策略），摄像头被禁用，无法打开
     * */
    ERROR_CAMERA_DISABLED,

    /**
     * 摄像头设备遇到致命错误，需要重新打开摄像头才能恢复使用
     * */
    ERROR_CAMERA_DEVICE,

    /**
     * 摄像头服务遇到致命错误，可能需要重启设备或重新初始化摄像头服务
     * */
    ERROR_CAMERA_SERVICE,
}