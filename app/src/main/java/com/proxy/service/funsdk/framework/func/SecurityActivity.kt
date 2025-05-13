package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.security.aes.CsAesUtils
import com.proxy.service.core.framework.system.security.md5.CsMd5Utils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.threadpool.base.thread.task.ICallable
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/11/14 15:13
 * @desc:
 */
class SecurityActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SecurityActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val test_txt = "测试数据"

    private val key: SecretKey = CsAesUtils.createSecretKey()
    private val iv: IvParameterSpec = CsAesUtils.createIvParameterSpec()
    private val encryptLoader = CsAesUtils.cbc()
        .pkcs5padding()
        .setSecretKeySpec(key)
        .setIvParameterSpec(iv)
        .createEncryptLoader()
    private val decryptLoader = CsAesUtils.cbc()
        .pkcs5padding()
        .setSecretKeySpec(key)
        .setIvParameterSpec(iv)
        .createDecryptLoader()

    private var value: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_security)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.md5 -> {
                CsTask.ioThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        CsLogger.d(CsMd5Utils.getMD5(resources.openRawResource(R.raw.test_loading)))
                        return ""
                    }
                })?.start()
            }

            R.id.aes_encrypt -> {
                encryptLoader.reset()
                value = encryptLoader.setSourceString(test_txt)
                    .getBase64String()
                CsLogger.d("value = $value")
            }

            R.id.aes_decrypt -> {
                value?.let {
                    decryptLoader.reset()
                    val content = decryptLoader.setSourceBase64String(it)
                        .getString()
                    CsLogger.d("content = $content")
                }
            }
        }
    }

}