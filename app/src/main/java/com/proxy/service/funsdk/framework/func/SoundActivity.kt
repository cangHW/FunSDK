package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.system.sound.CsSoundUtils
import com.proxy.service.core.framework.system.sound.info.SoundConfig
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/12/6 16:45
 * @desc:
 */
class SoundActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.sound_init -> {
                CsSoundUtils.init(SoundConfig.builder().build())
            }

            R.id.sound_load -> {
                CsSoundUtils.loadRes(soundTag, R.raw.sound)
            }

            R.id.sound_play -> {
                CsSoundUtils.play(soundTag)
            }

            R.id.sound_unload -> {
                CsSoundUtils.unload(soundTag)
            }

            R.id.sound_release -> {
                CsSoundUtils.release()
            }
        }
    }

}