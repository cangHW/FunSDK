#ifndef CS_APM_ANR_HANDLER_H
#define CS_APM_ANR_HANDLER_H

int cs_apm_anr_init(const char *marker_dir);

int cs_apm_anr_deinit(void);

// 返回 1 表示已检测到 ANR 信号，0 表示未检测到
// 调用后自动重置标志位
int cs_apm_anr_check_and_reset(void);

#endif
