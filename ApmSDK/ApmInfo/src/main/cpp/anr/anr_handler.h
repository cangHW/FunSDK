#ifndef CS_APM_ANR_HANDLER_H
#define CS_APM_ANR_HANDLER_H

int cs_apm_anr_init(void);

int cs_apm_anr_deinit(void);

// 返回并重置累计 SIGQUIT 次数；0 表示未检测到
int cs_apm_anr_check_and_reset(void);

#endif
