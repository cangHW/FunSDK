#include "anr_handler.h"
#include "../common/apm_signal.h"
#include "../common/apm_errno.h"

#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/syscall.h>

static volatile int g_anr_pending_count = 0;
static int g_signal_catcher_tid = -1;

static void anr_signal_handler(int signum, siginfo_t *si, void *uc);

static int find_signal_catcher_tid(void);

int cs_apm_anr_init(void) {
    g_anr_pending_count = 0;
    g_signal_catcher_tid = find_signal_catcher_tid();

    return cs_apm_signal_trace_register(anr_signal_handler);
}

int cs_apm_anr_deinit(void) {
    cs_apm_signal_trace_unregister();
    g_anr_pending_count = 0;
    return 0;
}

int cs_apm_anr_check_and_reset(void) {
    return __atomic_exchange_n(&g_anr_pending_count, 0, __ATOMIC_SEQ_CST);
}

static void anr_signal_handler(int signum, siginfo_t *si, void *uc) {
    (void) signum;
    (void) si;
    (void) uc;

    __atomic_fetch_add(&g_anr_pending_count, 1, __ATOMIC_SEQ_CST);

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
