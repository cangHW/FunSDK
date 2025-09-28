package com.proxy.service.core.application.startup

/**
 * 循环依赖异常
 *
 * @author: cangHX
 * @data: 2025/9/26 09:32
 * @desc:
 */
class CircularDependencyException(message: String) : Throwable(message)

/**
 * 初始化异常
 *
 * @author: cangHX
 * @data: 2025/9/28 11:57
 * @desc:
 */
class StartupTimeoutException(message: String) : Throwable(message)

/**
 * 优先级配置异常
 *
 * @author: cangHX
 * @data: 2025/9/28 14:45
 * @desc:
 */
class PriorityRangeException(message: String) : Throwable(message)

