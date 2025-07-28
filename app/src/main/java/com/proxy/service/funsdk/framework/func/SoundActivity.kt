package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.system.sound.CsSoundUtils
import com.proxy.service.core.framework.system.sound.info.SoundConfig
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkSoundBinding

/**
 * @author: cangHX
 * @data: 2024/12/6 16:45
 * @desc:
 */
class SoundActivity : BaseActivity<ActivityFrameworkSoundBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SoundActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val soundTag = "sound_tag"
    private var soundId: Int = -1

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkSoundBinding {
        return ActivityFrameworkSoundBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.sound_init -> {
                CsSoundUtils.init(SoundConfig.builder().build())
                binding?.content?.addData("sound", "初始化一个音效池子")
            }

            R.id.sound_load -> {
                CsSoundUtils.loadRes(soundTag, R.raw.sound)
                binding?.content?.addData("sound", "加载音效到池子")
            }

            R.id.sound_play -> {
                soundId = CsSoundUtils.play(soundTag)
                binding?.content?.addData("sound", "播放音效")
            }

            R.id.sound_pause -> {
                CsSoundUtils.pause(soundId)
                binding?.content?.addData("sound", "暂停音效")
            }

            R.id.sound_unload -> {
                CsSoundUtils.unload(soundTag)
                binding?.content?.addData("sound", "卸载音效")
            }

            R.id.sound_release -> {
                CsSoundUtils.release()
                binding?.content?.addData("sound", "释放一个音效池子")
            }
        }
    }

}