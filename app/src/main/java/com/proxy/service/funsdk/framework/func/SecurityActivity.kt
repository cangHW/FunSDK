package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import com.proxy.service.core.framework.system.security.aes.CsAesUtils
import com.proxy.service.core.framework.system.security.md5.CsMd5Utils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkSecurityBinding
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/11/14 15:13
 * @desc:
 */
class SecurityActivity : BaseActivity<ActivityFrameworkSecurityBinding>() {

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
    private var encryptLoader = CsAesUtils.cbc()
        .pkcs5padding()
        .setSecretKeySpec(key)
        .setIvParameterSpec(iv)
        .createEncryptLoader()
    private var decryptLoader = CsAesUtils.cbc()
        .pkcs5padding()
        .setSecretKeySpec(key)
        .setIvParameterSpec(iv)
        .createDecryptLoader()

    private var value: String? = null

    override fun initView() {
        binding?.aesModeSelect?.setOnCheckedChangeListener { _, checkedId ->
            value = null

            val loader = when (checkedId) {
                R.id.aes_cbc -> {
                    CsAesUtils.cbc().pkcs5padding().setSecretKeySpec(key).setIvParameterSpec(iv)
                }

                R.id.aes_cfb -> {
                    CsAesUtils.cfb().setSecretKeySpec(key).setIvParameterSpec(iv)
                }

                R.id.aes_ofb -> {
                    CsAesUtils.ofb().setSecretKeySpec(key).setIvParameterSpec(iv)
                }

                R.id.aes_ctr -> {
                    CsAesUtils.ctr().setSecretKeySpec(key).setIvParameterSpec(iv)
                }

                R.id.aes_ecb -> {
                    CsAesUtils.ecb().pkcs5padding().setSecretKeySpec(key)
                }

                R.id.aes_gcm -> {
                    CsAesUtils.gcm().setSecretKeySpec(key).setIvParameterSpec(iv)
                }

                R.id.aes_ccm -> {
                    CsAesUtils.ccm().pkcs5padding().setSecretKeySpec(key).setIvParameterSpec(iv)
                }

                else -> {
                    CsAesUtils.cbc().pkcs5padding().setSecretKeySpec(key).setIvParameterSpec(iv)
                }
            }
            encryptLoader = loader.createEncryptLoader()
            decryptLoader = loader.createDecryptLoader()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.md5 -> {
                binding?.content?.addData("md5", "开始处理 md5")

                if (binding?.md5String?.isChecked == true) {
                    val result = CsMd5Utils.getMD5(test_txt)
                    binding?.content?.addData("md5", "src=$test_txt, md5=$result")
                } else if (binding?.md5File?.isChecked == true) {
                    val file = File(getExternalFilesDir(null), "test_md5.file")
                    file.createNewFile()

                    val result = CsMd5Utils.getMD5(file)
                    binding?.content?.addData("md5", "src=${file.absolutePath}, md5=$result")
                } else if (binding?.md5Stream?.isChecked == true) {
                    CsTask.ioThread()?.call(object : ICallable<String> {
                        override fun accept(): String {
                            val result =
                                CsMd5Utils.getMD5(resources.openRawResource(R.raw.test_loading))
                            binding?.content?.addData("md5", "src=R.raw.test_loading, md5=$result")
                            return ""
                        }
                    })?.start()
                }
            }

            R.id.aes_encrypt -> {
                encryptLoader.reset()
                value = encryptLoader.setSourceString(test_txt).getString()
                binding?.content?.addData("aes", "加密 src=$test_txt, dest=$value")
            }

            R.id.aes_decrypt -> {
                if (value == null) {
                    binding?.content?.addData("aes", "待解密数据为空")
                    return
                }
                value?.let {
                    decryptLoader.reset()
                    val content = decryptLoader.setSourceString(it).getString()
                    binding?.content?.addData("aes", "解密 src=$it, dest=$content")
                }
            }
        }
    }

}