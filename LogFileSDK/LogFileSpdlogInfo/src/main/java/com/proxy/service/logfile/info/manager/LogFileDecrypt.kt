package com.proxy.service.logfile.info.manager

import com.proxy.service.core.framework.system.security.sha.CsSha256Utils

class LogFileDecrypt private constructor() {

    companion object {
        init {
            System.loadLibrary("logfile")
        }

        private val _instance by lazy {
            LogFileDecrypt()
        }

        fun getInstance(): LogFileDecrypt {
            return _instance
        }
    }

    fun decrypt(inputPath: String, outputPath: String, key: String): Boolean {
        val hexKey = CsSha256Utils.get(key)
        if (hexKey.isEmpty()) return false
        return nativeDecryptFile(inputPath, outputPath, hexKey)
    }

    private external fun nativeDecryptFile(inputPath: String, outputPath: String, hexKey: String): Boolean
}
