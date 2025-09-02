
#include <malloc.h>
#include <string.h>
#include <sys/syscall.h>
#include <unistd.h>
#include "apm_signal.h"
#include "apm_errno.h"

#define CS_APM_SIGNAL_CRASH_STACK_SIZE (1024 * 128)

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wpadded"
typedef struct {
    int signum;
    struct sigaction oldact;
} cs_apm_signal_crash_info_t;
#pragma clang diagnostic pop

static cs_apm_signal_crash_info_t cs_apm_signal_crash_info[] =
        {
                {.signum = SIGABRT},
                {.signum = SIGBUS},
                {.signum = SIGFPE},
                {.signum = SIGILL},
                {.signum = SIGSEGV},
                {.signum = SIGTRAP},
                {.signum = SIGSYS},
                {.signum = SIGSTKFLT}
        };


int cs_apm_signal_crash_register(void (*handler)(int, siginfo_t *, void *)) {
    stack_t ss;
    if (NULL == (ss.ss_sp = calloc(1, CS_APM_SIGNAL_CRASH_STACK_SIZE))) {
        return CS_APM_ERRNO_NO_MEMORY;
    }

    ss.ss_size = CS_APM_SIGNAL_CRASH_STACK_SIZE;
    ss.ss_flags = 0;
    if (0 != sigaltstack(&ss, NULL)) {
        return CS_APM_ERRNO_SYSTEM;
    }

    struct sigaction act;
    memset(&act, 0, sizeof(act));
    sigfillset(&act.sa_mask);
    act.sa_sigaction = handler;
    act.sa_flags = SA_RESTART | SA_SIGINFO | SA_ONSTACK;

    size_t array_size = sizeof(cs_apm_signal_crash_info) / sizeof(cs_apm_signal_crash_info[0]);
    cs_apm_signal_crash_info_t *info = cs_apm_signal_crash_info;
    cs_apm_signal_crash_info_t *end = cs_apm_signal_crash_info + array_size;

    for (; info < end; info++) {
        if (0 != sigaction(info->signum, &act, &(info->oldact))) {
            return CS_APM_ERRNO_SYSTEM;
        }
    }
    return 0;
}

int cs_apm_signal_crash_unregister(void) {
    int r = 0;

    size_t array_size = sizeof(cs_apm_signal_crash_info) / sizeof(cs_apm_signal_crash_info[0]);
    cs_apm_signal_crash_info_t *info = cs_apm_signal_crash_info;
    cs_apm_signal_crash_info_t *end = cs_apm_signal_crash_info + array_size;

    for (; info < end; info++) {
        if (0 != sigaction(info->signum, &(info->oldact), NULL)) {
            r = CS_APM_ERRNO_SYSTEM;
        }
    }
    return r;
}

int cs_apm_signal_crash_ignore(void) {
    struct sigaction act;
    memset(&act, 0, sizeof(act));
    sigemptyset(&act.sa_mask);
    act.sa_handler = SIG_DFL;
    act.sa_flags = SA_RESTART;

    int r = 0;

    size_t array_size = sizeof(cs_apm_signal_crash_info) / sizeof(cs_apm_signal_crash_info[0]);
    cs_apm_signal_crash_info_t *info = cs_apm_signal_crash_info;
    cs_apm_signal_crash_info_t *end = cs_apm_signal_crash_info + array_size;

    for (; info < end; info++) {
        if (0 != sigaction(info->signum, &act, NULL)) {
            r = CS_APM_ERRNO_SYSTEM;
        }
    }
    return r;
}

int cs_apm_signal_crash_queue(siginfo_t *si) {
    if (SIGABRT == si->si_signo || SI_FROMUSER(si)) {
        if (0 != syscall(SYS_rt_tgsigqueueinfo, getpid(), gettid(), si->si_signo, si)) {
            return CS_APM_ERRNO_SYSTEM;
        }
    }
    return 0;
}


static sigset_t cs_apm_signal_trace_oldset;
static struct sigaction cs_apm_signal_trace_oldact;

int cs_apm_signal_trace_register(void (*handler)(int, siginfo_t *, void *)) {
    int              r;
    sigset_t         set;
    struct sigaction act;

    //un-block the SIGQUIT mask for current thread, hope this is the main thread
    sigemptyset(&set);
    sigaddset(&set, SIGQUIT);
    if(0 != (r = pthread_sigmask(SIG_UNBLOCK, &set, &cs_apm_signal_trace_oldset))) {
        return r;
    }

    //register new signal handler for SIGQUIT
    memset(&act, 0, sizeof(act));
    sigfillset(&act.sa_mask);
    act.sa_sigaction = handler;
    act.sa_flags = SA_RESTART | SA_SIGINFO;
    if(0 != sigaction(SIGQUIT, &act, &cs_apm_signal_trace_oldact)){
        pthread_sigmask(SIG_SETMASK, &cs_apm_signal_trace_oldset, NULL);
        return CS_APM_ERRNO_SYSTEM;
    }
    return 0;
}

void cs_apm_signal_trace_unregister(void) {
    pthread_sigmask(SIG_SETMASK, &cs_apm_signal_trace_oldset, NULL);
    sigaction(SIGQUIT, &cs_apm_signal_trace_oldact, NULL);
}
