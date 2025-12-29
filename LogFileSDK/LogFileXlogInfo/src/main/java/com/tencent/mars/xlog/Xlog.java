package com.tencent.mars.xlog;

/**
 * Mars xlog 日志库 Java 封装
 * 提供高性能、可靠的日志记录功能
 */
public class Xlog {

    // 日志级别常量
    public static final int LEVEL_ALL = 0;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_NONE = 6;

    // Appender 模式
    public static final int AppenderModeAsync = 0;
    public static final int AppenderModeSync = 1;

    // 压缩模式
    public static final int ZLIB_MODE = 0;
    public static final int ZSTD_MODE = 1;

    static {
        try {
            System.loadLibrary("nativelib");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    /**
     * xlog 配置类
     */
    public static class XLogConfig {
        public int level = LEVEL_INFO;
        public int mode = AppenderModeAsync;
        public String logdir;
        public String nameprefix;
        public String pubkey = "";
        public int compressmode = ZLIB_MODE;
        public int compresslevel = 6;
        public String cachedir = "";
        public int cachedays = 0;
    }

    /**
     * 日志信息类
     */
    public static class XLoggerInfo {
        public int level;
        public String tag;
        public String filename;
        public String funcname;
        public int line;
        public long pid;
        public long tid;
        public long maintid;
    }

    // ==================== Native 方法 ====================

    /**
     * 打开 appender
     * @param config 配置
     */
    public static native void appenderOpen(XLogConfig config);

    /**
     * 关闭 appender
     */
    public static native void appenderClose();

    /**
     * 刷新日志
     * @param instancePtr 实例指针，0 表示默认实例
     * @param isSync 是否同步刷新
     */
    public static native void appenderFlush(long instancePtr, boolean isSync);

    /**
     * 写日志
     * @param logInfo 日志信息
     * @param log 日志内容
     */
    public static native void logWrite(XLoggerInfo logInfo, String log);

    /**
     * 写日志（多实例版本）
     * @param instancePtr 实例指针
     * @param level 日志级别
     * @param tag 标签
     * @param filename 文件名
     * @param funcname 函数名
     * @param line 行号
     * @param pid 进程ID
     * @param tid 线程ID
     * @param maintid 主线程ID
     * @param log 日志内容
     */
    public static native void logWrite2(long instancePtr, int level, String tag,
                                        String filename, String funcname, int line,
                                        int pid, long tid, long maintid, String log);

    /**
     * 设置是否输出到控制台
     * @param instancePtr 实例指针
     * @param isOpen 是否打开
     */
    public static native void setConsoleLogOpen(long instancePtr, boolean isOpen);

    /**
     * 设置最大文件大小
     * @param instancePtr 实例指针
     * @param maxSize 最大大小（字节）
     */
    public static native void setMaxFileSize(long instancePtr, long maxSize);

    /**
     * 设置最大存活时间
     * @param instancePtr 实例指针
     * @param maxTime 最大时间（秒）
     */
    public static native void setMaxAliveTime(long instancePtr, long maxTime);

    /**
     * 创建新的 xlog 实例
     * @param config 配置
     * @return 实例指针
     */
    public static native long newXlogInstance(XLogConfig config);

    /**
     * 获取 xlog 实例
     * @param nameprefix 名称前缀
     * @return 实例指针
     */
    public static native long getXlogInstance(String nameprefix);

    /**
     * 释放 xlog 实例
     * @param nameprefix 名称前缀
     */
    public static native void releaseXlogInstance(String nameprefix);

    // ==================== 便捷方法 ====================

    /**
     * 关闭 xlog
     */
    public static void close() {
        appenderClose();
    }

    /**
     * 刷新日志到文件
     * @param isSync 是否同步刷新
     */
    public static void flush(boolean isSync) {
        appenderFlush(0, isSync);
    }


}


