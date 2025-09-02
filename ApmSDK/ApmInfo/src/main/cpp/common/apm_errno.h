#ifndef FUNSDK_APM_ERRNO_H
#define FUNSDK_APM_ERRNO_H

#include <errno.h>

// 未知错误
#define CS_APM_ERRNO_UNKNOWN  1001
// 无效参数
#define CS_APM_ERRNO_INVALID    1002
// 内存分配失败
#define CS_APM_ERRNO_NO_MEMORY    1003
// 磁盘空间不足
#define CS_APM_ERRNO_NO_SPACE  1004
// 超出范围
#define CS_APM_ERRNO_RANGE    1005
// 未找到
#define CS_APM_ERRNO_NOT_FIND   1006
// 缺少必要的资源
#define CS_APM_ERRNO_MISSING  1007
// 内存相关错误
#define CS_APM_ERRNO_MEM      1008
// 设备相关错误
#define CS_APM_ERRNO_DEV      1009
// 权限不足
#define CS_APM_ERRNO_PERM     1010
// 格式错误
#define CS_APM_ERRNO_FORMAT   1011
// 非法操作
#define CS_APM_ERRNO_ILLEGAL  1012
// 不支持的操作
#define CS_APM_ERRNO_NOT_SUPPORT   1013
// 非法状态
#define CS_APM_ERRNO_STATE    1014
// JNI（Java Native Interface）相关错误
#define CS_APM_ERRNO_JNI      1015
// 文件描述符相关错误
#define CS_APM_ERRNO_FD       1016

// 系统错误码处理宏
#define CS_APM_ERRNO_SYSTEM     ((0 != errno) ? errno : CS_APM_ERRNO_UNKNOWN)


#endif //FUNSDK_APM_ERRNO_H
