package com.proxy.service.core.framework.system.device

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.File
import java.io.FileInputStream
import java.util.Properties


/**
 * 设备信息相关工具
 *
 * @author: cangHX
 * @data: 2024/7/25 17:26
 * @desc:
 */
object CsDeviceUtils {

    private const val TAG = "${Constants.TAG}Device"

    /**
     * 获取设备品牌名称
     * */
    fun getBrand(): String {
        return Build.BRAND ?: ""
    }

    /**
     * 获取设备型号名称
     * */
    fun getModel(): String {
        return Build.MODEL ?: ""
    }

    /**
     * 获取设备类型
     * */
    fun getDeviceType(): DeviceType {
        try {
            if (DeviceType.Vivo.check()) {
                return DeviceType.Vivo
            } else if (DeviceType.Oppo.check()) {
                return DeviceType.Oppo
            } else if (DeviceType.OnePlus.check()) {
                return DeviceType.OnePlus
            } else if (DeviceType.Realme.check()) {
                return DeviceType.Realme
            } else if (DeviceType.MeiZu.check()) {
                return DeviceType.MeiZu
            } else if (DeviceType.Xiaomi.check()) {
                return DeviceType.Xiaomi
            } else if (DeviceType.Huawei.check()) {
                return DeviceType.Huawei
            } else if (DeviceType.Honor.check()) {
                return DeviceType.Honor
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return DeviceType.UnKnown
    }

    /**
     * 获取系统类型
     * */
    fun getRomType(): RomType {
        try {
            val properties = Properties()
            properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
            if (RomType.Miui.check(properties)) {
                return RomType.Miui
            } else if (RomType.Emui.check(properties)) {
                return RomType.Emui
            } else if (RomType.FlyMe.check(properties)) {
                return RomType.FlyMe
            } else if (RomType.HarmonyOs.check(properties)) {
                return RomType.HarmonyOs
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return RomType.Android
    }

    private var activityManager: ActivityManager? = null
    private fun getActivityManager(): ActivityManager? {
        if (activityManager != null) {
            return activityManager
        }
        activityManager = CsContextManager.getApplication()
            .getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager?
        return activityManager
    }

    /**
     * 获取设备总内存, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     * */
    fun getTotalMemory(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        getActivityManager()?.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    /**
     * 获取设备可用内存, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     * */
    fun getAvailMemory(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        getActivityManager()?.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    /**
     * 获取当前应用已用内存（不包含共享内存）, 单位：KB, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     * */
    fun getAppUsePrivateMemory(): Int {
        val pids = intArrayOf(Process.myPid())
        val memoryInfos = getActivityManager()?.getProcessMemoryInfo(pids)
        memoryInfos?.firstOrNull()?.let {
            return it.totalPrivateDirty
        }
        return 0
    }

    /**
     * 获取当前应用已用内存（包含共享内存）, 单位：KB, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     * */
    fun getAppUseAllMemory(): Int {
        val pids = intArrayOf(Process.myPid())
        val memoryInfos = getActivityManager()?.getProcessMemoryInfo(pids)
        memoryInfos?.firstOrNull()?.let {
            return it.totalPss
        }
        return 0
    }

    /**
     * 获取设备总内部存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 需要权限：Manifest.permission.READ_EXTERNAL_STORAGE
     * */
    fun getInterTotalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }

    /**
     * 获取设备可用内部存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 需要权限：Manifest.permission.READ_EXTERNAL_STORAGE
     * */
    fun getInterAvailStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }

    /**
     * 获取设备总外部存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 需要权限：Manifest.permission.READ_EXTERNAL_STORAGE
     * */
    fun getOuterTotalStorage(): Long {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            return totalBlocks * blockSize
        }
        CsLogger.tag(TAG).i("外部存储未挂载")
        return 0
    }

    /**
     * 获取设备可用外部存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 需要权限：Manifest.permission.READ_EXTERNAL_STORAGE
     * */
    fun getOuterAvailStorage(): Long {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            return availableBlocks * blockSize
        }
        CsLogger.tag(TAG).i("外部存储未挂载")
        return 0
    }

}