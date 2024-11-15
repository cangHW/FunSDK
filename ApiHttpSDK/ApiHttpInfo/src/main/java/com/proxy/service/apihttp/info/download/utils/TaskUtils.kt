package com.proxy.service.apihttp.info.download.utils

import com.proxy.service.apihttp.base.download.task.DownloadTask

/**
 * @author: cangHX
 * @data: 2024/11/8 11:40
 * @desc:
 */
object TaskUtils {

    /**
     * 检查任务是否一致
     * */
    fun checkIsSame(src: DownloadTask, dest: DownloadTask): Boolean {
        if (src.getGroupName() != dest.getGroupName()) {
            return false
        }
        if (src.getPriority() != dest.getPriority()) {
            return false
        }
        if (src.getDownloadUrl() != dest.getDownloadUrl()) {
            return false
        }
        if (src.getFilePath() != dest.getFilePath()) {
            return false
        }
        if (src.getFileSize() != 0L && dest.getFileSize() != 0L) {
            if (src.getFileSize() != dest.getFileSize()) {
                return false
            }
        }
        if (src.getFileMd5().trim().isNotEmpty() && dest.getFileMd5().trim().isNotEmpty()) {
            if (src.getFileMd5() != dest.getFileMd5()) {
                return false
            }
        }
        return true
    }

}