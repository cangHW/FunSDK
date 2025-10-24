package com.proxy.service.logfile.info.manager

/**
 * @author: cangHX
 * @data: 2025/10/22 17:19
 * @desc:
 *
 * 算法类型	速度	            压缩率	        CPU使用	    内存使用	    移动端适配	推荐场景
 * LZ4	    🚀🚀🚀🚀🚀	    ⭐⭐⭐	        极低	        极低	        ✅完美	    高频日志
 * ZSTD	    🚀🚀🚀🚀	    ⭐⭐⭐⭐⭐	    低	        低	        ✅很好	    存储优化
 * Snappy	🚀🚀🚀🚀	    ⭐⭐⭐	        低	        中	        ✅很好	    实时压缩
 * GZIP	    🚀🚀	        ⭐⭐⭐⭐	    中	        中	        ✅好	    兼容性要求
 *
 */
enum class CompressionMode(val mode: Int) {

    /**
     * 不压缩
     * */
    NONE(0),

    /**
     * 适合高频日志场景
     * */
    LZ4(1);

//    /**
//     * 适合存储优化场景
//     * */
//    ZSTD(2);

}