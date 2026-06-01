#include "anr_handler.h"
#include "../common/apm_signal.h"
#include "../common/apm_errno.h"

#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <time.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/syscall.h>

static char g_marker_path[512];
static volatile int g_anr_detected = 0;
static int g_signal_catcher_tid = -1;

static void anr_signal_handler(int signum, siginfo_t *si, void *uc);

static int find_signal_catcher_tid(void);
static int itoa_simple(long val, char *buf);

int cs_apm_anr_init(const char *marker_dir) {
    if (marker_dir == NULL) {
        return CS_APM_ERRNO_INVALID;
    }

    int len = snprintf(g_marker_path, sizeof(g_marker_path),
                       "%s/anr_marker.tmp", marker_dir);
    if (len < 0 || len >= (int) sizeof(g_marker_path)) {
        return CS_APM_ERRNO_RANGE;
    }

    g_anr_detected = 0;
    g_signal_catcher_tid = find_signal_catcher_tid();

    int ret = cs_apm_signal_trace_register(anr_signal_handler);
    return ret;
}

int cs_apm_anr_deinit(void) {
    cs_apm_signal_trace_unregister();
    g_anr_detected = 0;
    return 0;
}

int cs_apm_anr_check_and_reset(void) {
    int val = __atomic_exchange_n(&g_anr_detected, 0, __ATOMIC_SEQ_CST);
    return val;
}

static void anr_signal_handler(int signum, siginfo_t *si, void *uc) {
    (void) signum;
    (void) si;
    (void) uc;

    // 设置标志位，Java 侧 Watchdog 轮询检测
    __atomic_store_n(&g_anr_detected, 1, __ATOMIC_SEQ_CST);

    // 写 marker 文件 (兜底：下次启动时检查)
    int fd = open(g_marker_path, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (fd >= 0) {
        char buf[64];
        int n;

        n = itoa_simple(getpid(), buf);
        buf[n++] = '\n';
        write(fd, buf, n);

        struct timespec ts;
        clock_gettime(CLOCK_REALTIME, &ts);
        n = itoa_simple((long) ts.tv_sec, buf);
        buf[n++] = '\n';
        write(fd, buf, n);

        fsync(fd);
        close(fd);
    }

    // 恢复原有 handler 并转发给 SignalCatcher 线程（使用 init 时缓存的 tid）
    cs_apm_signal_trace_unregister();

    if (g_signal_catcher_tid > 0) {
        syscall(__NR_tgkill, getpid(), g_signal_catcher_tid, SIGQUIT);
    }
}

static int find_signal_catcher_tid(void) {
    char path[128];
    snprintf(path, sizeof(path), "/proc/%d/task", getpid());

    DIR *dir = opendir(path);
    if (dir == NULL) return -1;

    struct dirent *entry;
    while ((entry = readdir(dir)) != NULL) {
        if (entry->d_name[0] == '.') continue;

        char comm_path[256];
        snprintf(comm_path, sizeof(comm_path), "/proc/%d/task/%s/comm",
                 getpid(), entry->d_name);

        int fd = open(comm_path, O_RDONLY);
        if (fd < 0) continue;

        char comm[32];
        ssize_t n = read(fd, comm, sizeof(comm) - 1);
        close(fd);

        if (n > 0) {
            comm[n] = '\0';
            // 去掉末尾换行
            if (n > 0 && comm[n - 1] == '\n') comm[n - 1] = '\0';

            if (strcmp(comm, "Signal Catcher") == 0) {
                closedir(dir);
                int tid = 0;
                const char *s = entry->d_name;
                while (*s >= '0' && *s <= '9') {
                    tid = tid * 10 + (*s - '0');
                    s++;
                }
                return tid;
            }
        }
    }

    closedir(dir);
    return -1;
}

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
