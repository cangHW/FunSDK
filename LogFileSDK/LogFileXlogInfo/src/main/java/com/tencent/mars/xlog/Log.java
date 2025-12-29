package com.tencent.mars.xlog;

import android.os.Process;

/**
 * xlog 日志便捷封装类
 * 使用方式类似 android.util.Log
 */
public class Log {

    private static final long MAIN_TID = Thread.currentThread().getId();

    /**
     * Verbose 日志
     */
    public static void v(String tag, String msg) {
        log(Xlog.LEVEL_VERBOSE, tag, msg);
    }

    public static void v(String tag, String format, Object... args) {
        log(Xlog.LEVEL_VERBOSE, tag, String.format(format, args));
    }

    /**
     * Debug 日志
     */
    public static void d(String tag, String msg) {
        log(Xlog.LEVEL_DEBUG, tag, msg);
    }

    public static void d(String tag, String format, Object... args) {
        log(Xlog.LEVEL_DEBUG, tag, String.format(format, args));
    }

    /**
     * Info 日志
     */
    public static void i(String tag, String msg) {
        log(Xlog.LEVEL_INFO, tag, msg);
    }

    public static void i(String tag, String format, Object... args) {
        log(Xlog.LEVEL_INFO, tag, String.format(format, args));
    }

    /**
     * Warning 日志
     */
    public static void w(String tag, String msg) {
        log(Xlog.LEVEL_WARNING, tag, msg);
    }

    public static void w(String tag, String format, Object... args) {
        log(Xlog.LEVEL_WARNING, tag, String.format(format, args));
    }

    public static void w(String tag, Throwable tr) {
        log(Xlog.LEVEL_WARNING, tag, android.util.Log.getStackTraceString(tr));
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(Xlog.LEVEL_WARNING, tag, msg + "\n" + android.util.Log.getStackTraceString(tr));
    }

    /**
     * Error 日志
     */
    public static void e(String tag, String msg) {
        log(Xlog.LEVEL_ERROR, tag, msg);
    }

    public static void e(String tag, String format, Object... args) {
        log(Xlog.LEVEL_ERROR, tag, String.format(format, args));
    }

    public static void e(String tag, Throwable tr) {
        log(Xlog.LEVEL_ERROR, tag, android.util.Log.getStackTraceString(tr));
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(Xlog.LEVEL_ERROR, tag, msg + "\n" + android.util.Log.getStackTraceString(tr));
    }

    /**
     * Fatal 日志
     */
    public static void f(String tag, String msg) {
        log(Xlog.LEVEL_FATAL, tag, msg);
    }

    public static void f(String tag, String format, Object... args) {
        log(Xlog.LEVEL_FATAL, tag, String.format(format, args));
    }

    public static void f(String tag, Throwable tr) {
        log(Xlog.LEVEL_FATAL, tag, android.util.Log.getStackTraceString(tr));
    }

    public static void f(String tag, String msg, Throwable tr) {
        log(Xlog.LEVEL_FATAL, tag, msg + "\n" + android.util.Log.getStackTraceString(tr));
    }

    /**
     * 打印日志（带调用栈信息）
     */
    public static void printErrStackTrace(String tag, Throwable tr, String format, Object... args) {
        String msg = (args == null || args.length == 0) ? format : String.format(format, args);
        log(Xlog.LEVEL_ERROR, tag, msg + "\n" + android.util.Log.getStackTraceString(tr));
    }

    /**
     * 核心日志方法
     */
    private static void log(int level, String tag, String msg) {
        if (msg == null) {
            msg = "";
        }

        // 获取调用栈信息
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String filename = "";
        String funcname = "";
        int line = 0;

        // 找到调用者（跳过 Log 类自身的方法）
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            String className = element.getClassName();
            if (!className.equals(Log.class.getName()) 
                && !className.equals(Thread.class.getName())
                && !className.contains("dalvik.system")) {
                filename = element.getFileName();
                funcname = element.getMethodName();
                line = element.getLineNumber();
                break;
            }
        }

        Xlog.logWrite2(
            0,  // 默认实例
            level,
            tag,
            filename != null ? filename : "",
            funcname,
            line,
            Process.myPid(),
            Thread.currentThread().getId(),
            MAIN_TID,
            msg
        );
    }
}


