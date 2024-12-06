package com.proxy.service.core.framework.system.sound

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.RawRes
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.system.sound.config.Config
import com.proxy.service.core.framework.system.sound.info.SoundConfig
import com.proxy.service.core.framework.system.sound.info.SoundInfo

/**
 * 音频工具类
 *
 * @author: cangHX
 * @data: 2024/12/5 20:06
 * @desc:
 */
object CsSoundUtils {

    private val soundConfigMap = HashMap<String, SoundConfig>()
    private val soundPoolMap = HashMap<String, SoundPool>()

    private val soundInfoMap = HashMap<String, SoundInfo>()

    /**
     * 初始化, 可以多次调用。
     * 根据 [SoundConfig] 中的 tag 信息, 可以初始化多套不同配置
     * */
    fun init(config: SoundConfig) {
        if (soundPoolMap.containsKey(config.getSoundPoolTag())) {
            return
        }

        val attributes = AudioAttributes.Builder()
        attributes.setUsage(config.getUsageType().usageType)
        attributes.setContentType(config.getContentType().contentType)

        val builder = SoundPool.Builder()
        builder.setMaxStreams(config.getMaxStreams())
        builder.setAudioAttributes(attributes.build())
        val soundPool = builder.build()

        soundPool.setOnLoadCompleteListener(
            LoadCompleteListener(
                config.getSoundPoolTag(),
                soundInfoMap
            )
        )

        soundPoolMap[config.getSoundPoolTag()] = soundPool
        soundConfigMap[config.getSoundPoolTag()] = config

        CsLogger.tag(Config.TAG).i("init success. soundPoolTag: ${config.getSoundPoolTag()}")
    }

    /**
     * 加载音频资源, 用于后续播放.
     * 资源大小建议不大于 100KB
     *
     * @param soundTag  音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param path      音频地址
     * */
    fun loadFile(soundTag: String, path: String, soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        if (!checkSoundTag(soundTag)) {
            return
        }

        if (!isCanLoadSound(soundTag)) {
            return
        }

        if (CsFileUtils.length(path) <= 0) {
            CsLogger.tag(Config.TAG).i("The sound file has an error. path: $path")
            return
        }

        val soundPool = getSoundPool(soundPoolTag) ?: return

        CsLogger.tag(Config.TAG).i("start load. soundTag: $soundTag")

        synchronized(soundInfoMap) {
            val id = soundPool.load(path, 1)
            if (id <= 0) {
                CsLogger.tag(Config.TAG).i("load error. soundTag: $soundTag")
                return
            }

            val info = SoundInfo(soundPoolTag)
            info.soundId = id
            soundInfoMap[soundTag] = info
        }
    }

    /**
     * 加载音频资源, 用于后续播放.
     * 资源大小建议不大于 100KB
     *
     * @param soundTag  音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param resId     音频资源 id
     * */
    fun loadRes(
        soundTag: String,
        @RawRes resId: Int,
        soundPoolTag: String = Config.DEFAULT_POOL_NAME
    ) {
        if (!checkSoundTag(soundTag)) {
            return
        }

        if (!isCanLoadSound(soundTag)) {
            return
        }

        val soundPool = getSoundPool(soundPoolTag) ?: return

        CsLogger.tag(Config.TAG).i("start load. soundTag: $soundTag")

        synchronized(soundInfoMap) {
            val id = soundPool.load(CsContextManager.getApplication(), resId, 1)
            if (id <= 0) {
                CsLogger.tag(Config.TAG).i("load error. soundTag: $soundTag")
                return
            }

            val info = SoundInfo(soundPoolTag)
            info.soundId = id
            soundInfoMap[soundTag] = info
        }
    }

    /**
     * 加载音频资源, 用于后续播放.
     * 资源大小建议不大于 100KB
     *
     * @param soundTag  音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param fileName  assets 资源路径与名称, 例如: media/asd.mp3
     * */
    fun loadAsset(
        soundTag: String,
        fileName: String,
        soundPoolTag: String = Config.DEFAULT_POOL_NAME
    ) {
        if (!checkSoundTag(soundTag)) {
            return
        }

        if (!isCanLoadSound(soundTag)) {
            return
        }

        if (fileName.trim().isEmpty()) {
            CsLogger.tag(Config.TAG).e("fileName can not be empty or blank")
            return
        }

        val soundPool = getSoundPool(soundPoolTag) ?: return

        CsLogger.tag(Config.TAG).i("start load. soundTag: $soundTag")

        try {
            val afd = CsContextManager.getApplication().assets.openFd(fileName)

            synchronized(soundInfoMap) {
                val id = soundPool.load(afd, 1)
                if (id <= 0) {
                    CsLogger.tag(Config.TAG).i("load error. soundTag: $soundTag")
                    return
                }

                val info = SoundInfo(soundPoolTag)
                info.soundId = id
                soundInfoMap[soundTag] = info
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
        }

    }

    /**
     * 播放音频（多次播放会生成不同播放 ID）
     *
     * @param soundTag  音频唯一标示
     * @return  播放 id(大于0), 可用于暂停本次播放等操作(-1 代表播放失败)
     * */
    fun play(soundTag: String): Int {
        val soundInfo = getSoundInfo(soundTag) ?: return -1
        val soundConfig = getSoundConfig(soundInfo.poolTag) ?: return -1
        return play(
            soundTag,
            soundConfig.getLeftVolume(),
            soundConfig.getRightVolume(),
            0,
            soundConfig.getRate()
        )
    }

    /**
     * 播放音频（多次播放会生成不同播放 ID）
     *
     * @param soundTag      音频唯一标示
     * @param leftVolume    左声道音量(0.0 - 1.0)
     * @param rightVolume   右声道音量(0.0 - 1.0)
     * @param loop          循环播放次数(0 不循环, -1 无限循环)
     * @param rate          播放速率(0.5 - 2.0)
     * @return  播放 id(大于0), 可用于暂停本次播放等操作(-1 代表播放失败)
     * */
    fun play(
        soundTag: String,
        leftVolume: Float,
        rightVolume: Float,
        loop: Int, rate: Float
    ): Int {
        val soundInfo = getSoundInfo(soundTag) ?: return -1
        if (!soundInfo.isReady) {
            return -1
        }
        val soundPool = getSoundPool(soundInfo.poolTag) ?: return -1
        return soundPool.play(soundInfo.soundId, leftVolume, rightVolume, 0, loop, rate)
    }

    /**
     * 暂停播放
     *
     * @param playId    播放 id, 播放时返回的 id
     * */
    fun pause(playId: Int, soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolTag) ?: return
        soundPool.pause(playId)
        CsLogger.tag(Config.TAG).i("pause success. playId: $playId")
    }

    /**
     * 暂停所有正在播放的音频
     * */
    fun pauseAll(soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolTag) ?: return
        soundPool.autoPause()
        CsLogger.tag(Config.TAG).i("autoPause success.")
    }

    /**
     * 继续播放
     *
     * @param playId    播放 id, 播放时返回的 id
     * */
    fun resume(playId: Int, soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolTag) ?: return
        soundPool.resume(playId)
        CsLogger.tag(Config.TAG).i("resume success. playId: $playId")
    }

    /**
     * 继续播放所有暂停的音频
     * */
    fun resumeAll(soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolTag) ?: return
        soundPool.autoResume()
        CsLogger.tag(Config.TAG).i("autoResume success.")
    }

    /**
     * 结束播放
     *
     * @param playId    播放 id, 播放时返回的 id
     * */
    fun stop(playId: Int, soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolTag) ?: return
        soundPool.stop(playId)
        CsLogger.tag(Config.TAG).i("stop success. playId: $playId")
    }

    /**
     * 卸载音频
     *
     * @param soundTag      音频唯一标示
     * */
    fun unload(soundTag: String) {
        val soundInfo = soundInfoMap.remove(soundTag) ?: return
        val soundPool = getSoundPool(soundInfo.poolTag) ?: return

        if (soundPool.unload(soundInfo.soundId)) {
            CsLogger.tag(Config.TAG).i("unload success. soundTag: $soundTag")
        }
    }

    /**
     * 清除全部资源
     * */
    fun release(soundPoolTag: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = soundPoolMap.remove(soundPoolTag) ?: return
        soundPool.release()
        soundConfigMap.remove(soundPoolTag)

        val iterator = soundInfoMap.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.poolTag == soundPoolTag) {
                iterator.remove()
            }
        }

        CsLogger.tag(Config.TAG).i("release success. soundPoolTag: $soundPoolTag")
    }

    private class LoadCompleteListener(
        private val poolTag: String,
        private val soundMap: HashMap<String, SoundInfo>
    ) : SoundPool.OnLoadCompleteListener {
        override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
            val iterator = synchronized(soundInfoMap) {
                soundMap.iterator()
            }
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val value = entry.value

                if (value.poolTag != poolTag) {
                    continue
                }

                if (value.soundId != sampleId) {
                    continue
                }

                if (status == 0) {
                    value.isReady = true
                    CsLogger.tag(Config.TAG).i("load success. sound tag: ${entry.key}")
                } else {
                    CsLogger.tag(Config.TAG)
                        .i("load failed. sound tag: ${entry.key}, status: $status")
                }
                return
            }
        }
    }

    /**
     * 检查 soundTag 是否正常
     * */
    private fun checkSoundTag(soundTag: String): Boolean {
        if (soundTag.trim().isEmpty()) {
            CsLogger.tag(Config.TAG).e("soundTag can not be empty or blank")
            return false
        }
        return true
    }

    /**
     * 是否可以加载该资源
     * */
    private fun isCanLoadSound(soundTag: String): Boolean {
        if (soundInfoMap.containsKey(soundTag)) {
            CsLogger.tag(Config.TAG)
                .e("The target sound does not need to be initialized repeatedly. soundTag: $soundTag")
            return false
        }
        return true
    }

    /**
     * 获取对应 SoundInfo
     * */
    private fun getSoundInfo(soundTag: String): SoundInfo? {
        val soundInfo = soundInfoMap[soundTag]
        if (soundInfo == null) {
            CsLogger.tag(Config.TAG)
                .e("The target sound is not loaded. soundTag: $soundTag")
            return null
        }
        return soundInfo
    }

    /**
     * 获取对应 SoundPool
     * */
    private fun getSoundPool(soundPoolTag: String): SoundPool? {
        val soundPool = soundPoolMap[soundPoolTag]
        if (soundPool == null) {
            CsLogger.tag(Config.TAG)
                .e("The target soundPool has not been initialized. soundPoolTag: $soundPoolTag")
            return null
        }
        return soundPool
    }

    /**
     * 获取对应 SoundConfig
     * */
    private fun getSoundConfig(soundPoolTag: String): SoundConfig? {
        val soundConfig = soundConfigMap[soundPoolTag]
        if (soundConfig == null) {
            CsLogger.tag(Config.TAG)
                .e("The target soundPool has not been initialized. soundPoolTag: $soundPoolTag")
            return null
        }
        return soundConfig
    }
}