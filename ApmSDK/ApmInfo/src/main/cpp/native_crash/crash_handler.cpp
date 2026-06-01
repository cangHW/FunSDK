#include "crash_handler.h"
#include "../common/apm_signal.h"
#include "../common/apm_errno.h"

#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <time.h>
#include <sys/types.h>

static char g_marker_path[512];
static int g_marker_fd = -1;

static void crash_signal_handler(int signum, siginfo_t *si, void *uc);

static int itoa_simple(long val, char *buf) {
    char tmp[24];
    int i = 0;
    unsigned long uval = (unsigned long) val;
    do {
        tmp[i++] = (char) ('0' + (int) (uval % 10));
        uval /= 10;
    } while (uval);
    int len = 0;
    while (i > 0) buf[len++] = tmp[--i];
    buf[len] = '\0';
    return len;
}

int cs_apm_native_crash_init(const char *tombstone_dir) {
    if (tombstone_dir == NULL) {
        return CS_APM_ERRNO_INVALID;
    }

    int len = snprintf(g_marker_path, sizeof(g_marker_path),
                       "%s/native_crash_marker.tmp", tombstone_dir);
    if (len < 0 || len >= (int) sizeof(g_marker_path)) {
        return CS_APM_ERRNO_RANGE;
    }

    g_marker_fd = open(g_marker_path, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (g_marker_fd < 0) {
        return CS_APM_ERRNO_FD;
    }

    int ret = cs_apm_signal_crash_register(crash_signal_handler);
    if (ret != 0) {
        close(g_marker_fd);
        g_marker_fd = -1;
        return ret;
    }

    return 0;
}

int cs_apm_native_crash_deinit(void) {
    cs_apm_signal_crash_unregister();

    if (g_marker_fd >= 0) {
        close(g_marker_fd);
        g_marker_fd = -1;
    }

    return 0;
}

static void crash_signal_handler(int signum, siginfo_t *si, void *uc) {
    (void) si;
    (void) uc;

    static volatile int handling = 0;
    if (__atomic_exchange_n(&handling, 1, __ATOMIC_SEQ_CST)) {
        _exit(1);
    }

    // 写入 marker 文件：pid\ntimestamp\nsignum\n
    if (g_marker_fd >= 0) {
        char buf[64];
        int n;

        lseek(g_marker_fd, 0, SEEK_SET);

        n = itoa_simple(getpid(), buf);
        buf[n++] = '\n';
        write(g_marker_fd, buf, n);

        struct timespec ts;
        clock_gettime(CLOCK_REALTIME, &ts);
        n = itoa_simple((long) ts.tv_sec, buf);
        buf[n++] = '\n';
        write(g_marker_fd, buf, n);

        n = itoa_simple(signum, buf);
        buf[n++] = '\n';
        write(g_marker_fd, buf, n);

        fsync(g_marker_fd);
    }

    // 恢复原有 handler (debuggerd)
    cs_apm_signal_crash_unregister();

    // 同步信号：直接 return，CPU 重新执行出错指令，debuggerd 以原始上下文接管
    // SIGABRT：异步信号，必须 re-raise
    if (signum == SIGABRT) {
        raise(signum);
    }
}
