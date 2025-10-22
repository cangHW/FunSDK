package com.proxy.service.logfile.info.manager

/**
 * @author: cangHX
 * @data: 2025/10/22 17:27
 * @desc:
 */
enum class EncryptionMode(val mode:String){

    NONE(""),

    ChaCha20("chacha20"),

    AES_GCM("aes_gcm"),

    AES_CBC("aes_cbc");

}