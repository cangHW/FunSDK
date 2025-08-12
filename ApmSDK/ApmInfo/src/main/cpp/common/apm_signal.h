
#ifndef FUNSDK_APM_SIGNAL_H
#define FUNSDK_APM_SIGNAL_H


#include <signal.h>

// 注册一个信号处理器，用于捕获特定的信号
int cs_apm_signal_crash_register(void (*handler)(int, siginfo_t *, void *));
// 注销信号处理器，恢复默认的信号处理行为
int cs_apm_signal_crash_unregister(void);
// 忽略特定信号
int cs_apm_signal_crash_ignore(void);
// 将信号信息加入队列，用于记录或延迟处理信号。
int cs_apm_signal_crash_queue(siginfo_t* si);

// 注册一个信号处理器，用于捕获特定信号并生成堆栈跟踪（trace）
int cs_apm_signal_trace_register(void (*handler)(int, siginfo_t *, void *));
// 注销信号跟踪处理器
void cs_apm_signal_trace_unregister(void);


#endif //FUNSDK_APM_SIGNAL_H
