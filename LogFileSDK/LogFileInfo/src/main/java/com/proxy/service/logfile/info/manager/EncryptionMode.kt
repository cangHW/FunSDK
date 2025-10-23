package com.proxy.service.logfile.info.manager

/**
 * @author: cangHX
 * @data: 2025/10/22 17:27
 * @desc:
 *
 * 算法类型	    速度	            安全性	        CPU使用	    现代性	移动端优化	推荐场景
 * ChaCha20	    🚀🚀🚀🚀🚀	    🔐🔐🔐🔐🔐	    极低	        现代	    ✅完美	    移动应用
 * AES-GCM	    🚀🚀🚀🚀	    🔐🔐🔐🔐🔐	    低	        经典  	✅很好	    企业标准
 * AES-CBC	    🚀🚀🚀	        🔐🔐🔐🔐	    中	        传统	    ✅好	    兼容性要求
 *
 */
enum class EncryptionMode(val mode: Int) {

    /**
     * 不加密
     * */
    NONE(0),

    /**
     * 适合移动应用场景
     * */
    CHA_CHA20(1),

    /**
     * 适合企业标准场景
     * */
    AES_GCM(2),

    /**
     * 适合兼容性要求场景
     * */
    AES_CBC(3);

}