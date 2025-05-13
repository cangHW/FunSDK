package com.proxy.service.core.framework.system.device

import android.Manifest
import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import java.util.UUID


/**
 * 设备信息相关工具
 *
 * @author: cangHX
 * @data: 2024/7/25 17:26
 * @desc:
 */
object CsDeviceUtils {

    private const val TAG = "${CoreConfig.TAG}Device"

    /**
     * 获取设备品牌名称
     * */
    fun getDeviceBrand(): String {
        return Build.BRAND ?: ""
    }

    /**
     * 获取设备型号名称
     * */
    fun getDeviceModel(): String {
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
     * 获取设备总存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 某些情况下需要权限：Manifest.permission.PACKAGE_USAGE_STATS
     * */
    fun getDeviceTotalStorage(): Long {
        val context = CsContextManager.getApplication()
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE)
        var total = 0L
        try {
            val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes")
            val getVolumeInfo = (getVolumes.invoke(storageManager) as? List<Any>?) ?: ArrayList()
            for (obj in getVolumeInfo) {
                val getType = obj.javaClass.getField("type")
                val type = getType.getInt(obj)
                if (type == 1) {
                    // 内置存储
                    var totalSize = 0L
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //8.0
                        val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                        val fsUuid = getFsUuid.invoke(obj) as? String?
                        totalSize = getTotalSizeWithFsUuid(context, fsUuid) //8.0 以后使用
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) { //7.1.1
                        val getPrimaryStorageSize =
                            StorageManager::class.java.getMethod("getPrimaryStorageSize") //5.0 6.0 7.0没有
                        totalSize = (getPrimaryStorageSize.invoke(storageManager) as? Long?) ?: 0
                    }

                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = (isMountedReadable.invoke(obj) as? Boolean?) ?: false
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as? File?
                        if (totalSize == 0L) {
                            totalSize = f?.totalSpace ?: 0
                        }
                        total += totalSize
                    }
                } else if (type == 0) {
                    // 外置存储
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = (isMountedReadable.invoke(obj) as? Boolean?) ?: false
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as? File?
                        total += f?.totalSpace ?: 0
                    }
                }
            }
        } catch (exception: SecurityException) {
            CsLogger.tag(TAG).e(
                exception,
                "Please check permissions. permission: ${Manifest.permission.PACKAGE_USAGE_STATS}"
            )
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return total
    }

    /**
     * 获取设备可用存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 某些情况下需要权限：Manifest.permission.PACKAGE_USAGE_STATS
     * */
    fun getDeviceAvailStorage(): Long {
        val context = CsContextManager.getApplication()
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE)
        var total = 0L
        var used = 0L
        try {
            val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes") //6.0
            val getVolumeInfo = (getVolumes.invoke(storageManager) as? List<Any>?) ?: ArrayList()
            for (obj in getVolumeInfo) {
                val getType = obj.javaClass.getField("type")
                val type = getType.getInt(obj)
                if (type == 1) {
                    // 内置存储
                    var totalSize = 0L
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //8.0
                        val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                        val fsUuid = getFsUuid.invoke(obj) as? String?
                        totalSize = getTotalSizeWithFsUuid(context, fsUuid) //8.0 以后使用
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) { //7.1.1
                        val getPrimaryStorageSize =
                            StorageManager::class.java.getMethod("getPrimaryStorageSize") //5.0 6.0 7.0没有
                        totalSize = (getPrimaryStorageSize.invoke(storageManager) as? Long?) ?: 0
                    }
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = (isMountedReadable.invoke(obj) as? Boolean?) ?: false
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as? File?
                        if (totalSize == 0L) {
                            totalSize = f?.totalSpace ?: 0
                        }
                        used += totalSize - (f?.freeSpace ?: 0)
                        total += totalSize
                    }
                } else if (type == 0) {
                    // 外置存储
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = (isMountedReadable.invoke(obj) as? Boolean?) ?: false
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as? File?
                        used += (f?.totalSpace ?: 0) - (f?.freeSpace ?: 0)
                        total += f?.totalSpace ?: 0
                    }
                }
            }
        } catch (exception: SecurityException) {
            CsLogger.tag(TAG).e(
                exception,
                "Please check permissions. permission: ${Manifest.permission.PACKAGE_USAGE_STATS}"
            )
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return total - used
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTotalSizeWithFsUuid(context: Context, fsUuid: String?): Long {
        try {
            val id = if (fsUuid == null) {
                StorageManager.UUID_DEFAULT
            } else {
                UUID.fromString(fsUuid)
            }
            val stats = context.getSystemService(
                StorageStatsManager::class.java
            )
            return stats.getTotalBytes(id)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return -1
    }

    /**
     * 获取设备SD总存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 需要权限：Manifest.permission.READ_EXTERNAL_STORAGE
     * */
    fun getExternalTotalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }

    /**
     * 获取设备SD可用存储, 单位：B, 可以使用 [com.proxy.service.core.framework.convert.CsStorageUnit] 转换为需要的单位.
     *
     * 需要权限：Manifest.permission.READ_EXTERNAL_STORAGE
     * */
    fun getExternalAvailStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }

}