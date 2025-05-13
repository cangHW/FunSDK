package com.proxy.service.core.framework.io.monitor.constants

import android.os.FileObserver
import android.os.FileObserver.CLOSE_NOWRITE
import android.os.FileObserver.CLOSE_WRITE

/**
 * @author: cangHX
 * @data: 2025/4/23 15:31
 * @desc:
 */
object Constants {

    const val MASK = FileObserver.CREATE or
            CLOSE_WRITE  or
            CLOSE_NOWRITE  or
            FileObserver.MODIFY or
            FileObserver.ATTRIB or
            FileObserver.DELETE or
            FileObserver.DELETE_SELF or
            FileObserver.MOVED_FROM or
            FileObserver.MOVED_TO

    const val TASK_NAME_START = "file_monitor_"
}