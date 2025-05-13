package com.proxy.service.apm.info.utils

/**
 * @author: cangHX
 * @data: 2025/4/16 09:57
 * @desc:
 */
object MemInfoDocUtils {

    fun findDoc(content: String): String {
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
        } else  if (content.startsWith("Percpu:")) {
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