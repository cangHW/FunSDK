#ifndef CS_APM_CRASH_HANDLER_H
#define CS_APM_CRASH_HANDLER_H

int cs_apm_native_crash_init(const char *tombstone_dir);

int cs_apm_native_crash_deinit(void);

#endif
