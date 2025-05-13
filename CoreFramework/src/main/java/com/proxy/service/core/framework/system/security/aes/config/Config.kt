package com.proxy.service.core.framework.system.security.aes.config

import com.proxy.service.core.constants.CoreConfig

/**
 * @author: cangHX
 * @data: 2024/12/2 21:19
 * @desc:
 */
object Config {

    const val TAG = "${CoreConfig.TAG}AES"

    const val ALGORITHM_AES = "AES"

    const val FEED_BACK_CBC = "CBC"
    const val FEED_BACK_CFB = "CFB"
    const val FEED_BACK_OFB = "OFB"
    const val FEED_BACK_CTR = "CTR"
    const val FEED_BACK_ECB = "ECB"
    const val FEED_BACK_GCM = "GCM"
    const val FEED_BACK_CCM = "CCM"

    const val PADDING_NO = "NoPadding"
    const val PADDING_PKCS5 = "PKCS5Padding"
    const val PADDING_PKCS7 = "PKCS7Padding"
    const val PADDING_ZERO = "ZeroPadding"
    const val PADDING_ISO10126 = "ISO10126Padding"
}