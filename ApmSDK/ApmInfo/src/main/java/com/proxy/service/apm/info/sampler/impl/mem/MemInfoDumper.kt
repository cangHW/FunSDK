package com.proxy.service.apm.info.sampler.impl.mem

import android.os.Debug
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.convert.CsStorageUnit
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.BufferedReader
import java.io.FileReader
import java.util.Locale

/**
 * 同步采集应用 PSS 与 /proc/meminfo 关键行
 */
object MemInfoDumper {

    private const val TAG = "${Constants.TAG}MemInfoDumper"
    private const val PROC_READ_TIMEOUT_MS = 200L
    private const val FORMAT_STR_18 = "%-18s %8s"
    private const val FORMAT_STR_35 = "%-35s %2s"

    private val MEMINFO_KEY_PREFIXES = arrayOf(
        "MemTotal:",
        "MemFree:",
        "MemAvailable:",
        "Buffers:",
        "Cached:",
        "SwapTotal:",
        "SwapFree:",
        "LowMem",
    )

    data class DumpResult(
        val appSectionText: String,
        val systemSectionText: String,
        val errorHint: String?,
    )

    fun dumpSync(): DumpResult {
        val appBuilder = StringBuilder()
        val systemBuilder = StringBuilder()
        val hints = ArrayList<String>()

        appendAppSection(appBuilder, hints)
        appendSystemKeyLines(systemBuilder, hints)

        val errorHint = if (hints.isEmpty()) {
            null
        } else {
            hints.joinToString("; ")
        }
        return DumpResult(
            appSectionText = appBuilder.toString(),
            systemSectionText = systemBuilder.toString(),
            errorHint = errorHint,
        )
    }

    private fun appendAppSection(builder: StringBuilder, hints: MutableList<String>) {
        builder.append("应用信息:").append("\n")
        try {
            val runtime = Runtime.getRuntime()
            val maxMemory = CsStorageUnit.B_UNIT_1000.toMbLong(runtime.maxMemory())
            builder.append(format("Runtime max:", "$maxMemory"))
                .append(" MB").append("\t\t").append("Java堆上限").append("\n")

            val totalMemory = CsStorageUnit.B_UNIT_1000.toMbLong(runtime.totalMemory())
            builder.append(format("Runtime total:", "$totalMemory"))
                .append(" MB").append("\t\t").append("Java堆当前总量").append("\n")

            val freeMemory = CsStorageUnit.B_UNIT_1000.toMbLong(runtime.freeMemory())
            builder.append(format("Runtime free:", "$freeMemory"))
                .append(" MB").append("\t\t").append("Java堆当前空闲").append("\n")

            val mi = Debug.MemoryInfo()
            Debug.getMemoryInfo(mi)

            val javaHeapKb = mi.getMemoryStat("summary.java-heap").toLongOrNull() ?: 0L
            val javaHeapMb = CsStorageUnit.B_UNIT_1000.toMbLong(javaHeapKb)
            builder.append(format("Java Heap:", "$javaHeapMb"))
                .append(" MB").append("\t\t").append("Java堆内存").append("\n")

            val nativeHeapKb = mi.getMemoryStat("summary.native-heap").toLongOrNull() ?: 0L
            val nativeHeapMb = CsStorageUnit.B_UNIT_1000.toMbLong(nativeHeapKb)
            builder.append(format("Native Heap:", "$nativeHeapMb"))
                .append(" MB").append("\t\t").append("Native堆内存").append("\n")

            val codeKb = mi.getMemoryStat("summary.code").toLongOrNull() ?: 0L
            val codeMb = CsStorageUnit.B_UNIT_1000.toMbLong(codeKb)
            builder.append(format("Code:", "$codeMb"))
                .append(" MB").append("\t\t").append("代码内存").append("\n")

            val stackKb = mi.getMemoryStat("summary.stack").toLongOrNull() ?: 0L
            val stackMb = CsStorageUnit.B_UNIT_1000.toMbLong(stackKb)
            builder.append(format("Stack:", "$stackMb"))
                .append(" MB").append("\t\t").append("栈内存").append("\n")

            val graphicsKb = mi.getMemoryStat("summary.graphics").toLongOrNull() ?: 0L
            val graphicsMb = CsStorageUnit.B_UNIT_1000.toMbLong(graphicsKb)
            builder.append(format("Graphics:", "$graphicsMb"))
                .append(" MB").append("\t\t").append("图形内存").append("\n")

            val privateOtherKb = mi.getMemoryStat("summary.private-other").toLongOrNull() ?: 0L
            val privateOtherMb = CsStorageUnit.B_UNIT_1000.toMbLong(privateOtherKb)
            builder.append(format("Private Other:", "$privateOtherMb"))
                .append(" MB").append("\t\t").append("其他私有内存").append("\n")

            val systemKb = mi.getMemoryStat("summary.system").toLongOrNull() ?: 0L
            val systemMb = CsStorageUnit.B_UNIT_1000.toMbLong(systemKb)
            builder.append(format("System:", "$systemMb"))
                .append(" MB").append("\t\t").append("系统内存").append("\n")

            val totalPssKb = mi.getMemoryStat("summary.total-pss").toLongOrNull() ?: 0L
            val totalPssMb = CsStorageUnit.B_UNIT_1000.toMbLong(totalPssKb)
            builder.append(format("TOTAL:", "$totalPssMb"))
                .append(" MB").append("\t\t").append("总PSS内存").append("\n")

            val totalSwapKb = mi.getMemoryStat("summary.total-swap").toLongOrNull() ?: 0L
            val totalSwapMb = CsStorageUnit.B_UNIT_1000.toMbLong(totalSwapKb)
            builder.append(format("TOTAL SWAP:", "$totalSwapMb"))
                .append(" MB").append("\t\t").append("总交换内存").append("\n")
        } catch (t: Throwable) {
            CsLogger.tag(TAG).e(t)
            hints.add("app mem: ${t.message}")
            builder.append("(app mem dump failed: ${t.message})").append("\n")
        }
    }

    private fun appendSystemKeyLines(builder: StringBuilder, hints: MutableList<String>) {
        builder.append("系统信息:").append("\n")
        val startMs = System.currentTimeMillis()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/meminfo"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (System.currentTimeMillis() - startMs > PROC_READ_TIMEOUT_MS) {
                    hints.add("proc meminfo read timeout")
                    builder.append("... meminfo read truncated (timeout)\n")
                    break
                }
                val current = line ?: continue
                if (!isKeyMeminfoLine(current)) {
                    continue
                }
                builder.append(current).append("\t\t").append(findDoc(current)).append("\n")
            }
        } catch (t: Throwable) {
            CsLogger.tag(TAG).e(t)
            hints.add("proc meminfo: ${t.message}")
            builder.append("(proc meminfo failed: ${t.message})").append("\n")
        } finally {
            CsFileUtils.close(reader)
        }
    }

    private fun isKeyMeminfoLine(line: String): Boolean {
        for (prefix in MEMINFO_KEY_PREFIXES) {
            if (line.startsWith(prefix)) {
                return true
            }
        }
        return false
    }

    private fun format(name: String, value: String): String {
        return String.format(Locale.US, FORMAT_STR_18, name, value)
    }

    private fun findDoc(content: String): String {
        if (content.startsWith("MemTotal:")) {
            return "总内存"
        } else if (content.startsWith("MemFree:")) {
            return "空闲内存"
        } else if (content.startsWith("MemAvailable:")) {
            return "可用内存"
        } else if (content.startsWith("Buffers:")) {
            return "给文件的缓冲大小"
        } else if (content.startsWith("Cached:")) {
            return "高速缓冲存储器"
        } else if (content.startsWith("SwapCached:")) {
            return "被高速缓冲存储用的交换空间的大小"
        } else if (content.startsWith("Active:")) {
            return "活跃使用中的高速缓冲存储器页面文件大小"
        } else if (content.startsWith("Inactive:")) {
            return "不经常使用中的告诉缓冲存储器文件大小"
        } else if (content.startsWith("Active(anon):")) {
            return "活跃的匿名内存（进程中堆上分配的内存,是用malloc分配的内存）"
        } else if (content.startsWith("Inactive(anon):")) {
            return "不活跃的匿名内存"
        } else if (content.startsWith("Active(file):")) {
            return "活跃的file内存"
        } else if (content.startsWith("Inactive(file):")) {
            return "不活跃的file内存"
        } else if (content.startsWith("Unevictable:")) {
            return "不能被释放的内存页"
        } else if (content.startsWith("Mlocked:")) {
            return "mlock()系统调用锁定的内存大小"
        } else if (content.startsWith("SwapTotal:")) {
            return "交换空间总大小"
        } else if (content.startsWith("SwapFree:")) {
            return "空闲交换空间"
        } else if (content.startsWith("Dirty:")) {
            return "等待被写回到磁盘的大小"
        } else if (content.startsWith("Writeback:")) {
            return "正在被写回的大小"
        } else if (content.startsWith("AnonPages:")) {
            return "未映射页的大小"
        } else if (content.startsWith("Mapped:")) {
            return "设备和文件映射大小"
        } else if (content.startsWith("Shmem:")) {
            return "已经被分配的共享内存大小"
        } else if (content.startsWith("KReclaimable:")) {
            return "可回收的内存"
        } else if (content.startsWith("Slab:")) {
            return "内核数据结构缓存大小"
        } else if (content.startsWith("SReclaimable:")) {
            return "可收回slab的大小"
        } else if (content.startsWith("SUnreclaim:")) {
            return "不可回收的slab的大小"
        } else if (content.startsWith("KernelStack:")) {
            return "kernel消耗的内存"
        } else if (content.startsWith("ShadowCallStack:")) {
            return "影子调用栈"
        } else if (content.startsWith("PageTables:")) {
            return "管理内存分页的索引表的大小"
        } else if (content.startsWith("NFS_Unstable:")) {
            return "不稳定页表的大小"
        } else if (content.startsWith("Bounce:")) {
            return "在低端内存中分配一个临时buffer作为跳转，把位于高端内存的缓存数据复制到此处消耗的内存"
        } else if (content.startsWith("WritebackTmp:")) {
            return "USE用于临时写回缓冲区的内存"
        } else if (content.startsWith("CommitLimit:")) {
            return "系统实际可分配内存总量"
        } else if (content.startsWith("Committed_AS:")) {
            return "当前已分配的内存总量"
        } else if (content.startsWith("VmallocTotal:")) {
            return "虚拟内存大小"
        } else if (content.startsWith("VmallocUsed:")) {
            return "已经被使用的虚拟内存大小"
        } else if (content.startsWith("VmallocChunk:")) {
            return "malloc 可分配的最大的逻辑连续的内存大小"
        } else if (content.startsWith("Percpu:")) {
            return "每个CPU的内存"
        } else if (content.startsWith("HardwareCorrupted:")) {
            return "删除掉的内存页的总大小(当系统检测到内存的硬件故障时)"
        } else if (content.startsWith("AnonHugePages:")) {
            return "匿名 HugePages 数量"
        } else if (content.startsWith("ShmemHugePages:")) {
            return "共享内存 HugePages 数量"
        } else if (content.startsWith("ShmemPmdMapped:")) {
            return "共享内存 PMD 映射数量"
        } else if (content.startsWith("FileHugePages:")) {
            return "文件 HugePages 数量"
        } else if (content.startsWith("FilePmdMapped:")) {
            return "文件 PMD 映射数量"
        } else if (content.startsWith("CmaTotal:")) {
            return "总的连续可用内存"
        } else if (content.startsWith("CmaFree:")) {
            return "空闲的连续内存"
        } else if (content.startsWith("HugePages_Total:")) {
            return "预留HugePages的总个数"
        } else if (content.startsWith("HugePages_Free:")) {
            return "池中尚未分配的 HugePages 数量"
        } else if (content.startsWith("HugePages_Rsvd:")) {
            return "表示池中已经被应用程序分配但尚未使用的 HugePages 数量"
        } else if (content.startsWith("HugePages_Surp:")) {
            return "这个值得意思是当开始配置了20个大页，现在修改配置为16，那么这个参数就会显示为4，一般不修改配置，这个值都是0"
        } else if (content.startsWith("Hugepagesize:")) {
            return "每个大页的大小"
        } else if (content.startsWith("DirectMap4k:")) {
            return "映射TLB为4kB的内存数量"
        } else if (content.startsWith("DirectMap2M:")) {
            return "映射TLB为2M的内存数量"
        } else if (content.startsWith("DirectMap1G:")) {
            return "映射TLB为1G的内存数量"
        }

        return ""
    }
}
