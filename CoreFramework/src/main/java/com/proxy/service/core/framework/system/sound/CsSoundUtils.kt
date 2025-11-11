package com.proxy.service.core.framework.system.sound

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.FloatRange
import androidx.annotation.RawRes
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.system.sound.config.Config
import com.proxy.service.core.framework.system.sound.info.SoundConfig
import com.proxy.service.core.framework.system.sound.info.SoundInfo
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

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
     * 根据 [SoundConfig] 中的 soundPoolName 信息, 可以初始化多套不同配置
     * */
    fun init(config: SoundConfig) {
        if (soundPoolMap.containsKey(config.getSoundPoolName())) {
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
                config.getSoundPoolName(),
                soundInfoMap
            )
        )

        soundPoolMap[config.getSoundPoolName()] = soundPool
        soundConfigMap[config.getSoundPoolName()] = config

        CsLogger.tag(Config.TAG).i("init success. soundPoolName: ${config.getSoundPoolName()}")
    }

    /**
     * 加载音频资源, 用于后续播放.
     * 资源大小建议不大于 100KB
     *
     * @param soundTag  音频唯一标示, 后续可以通过此标示控制音频的播放、释放等.
     * @param path      音频地址
     * */
    fun loadFile(soundTag: String, path: String, soundPoolName: String = Config.DEFAULT_POOL_NAME) {
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

        val soundPool = getSoundPool(soundPoolName) ?: return

        CsLogger.tag(Config.TAG).i("start load. soundTag: $soundTag")

        synchronized(soundInfoMap) {
            val id = soundPool.load(path, 1)
            if (id <= 0) {
                CsLogger.tag(Config.TAG).i("load error. soundTag: $soundTag")
                return
            }

            val info = SoundInfo(soundPoolName)
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
        soundPoolName: String = Config.DEFAULT_POOL_NAME
    ) {
        if (!checkSoundTag(soundTag)) {
            return
        }

        if (!isCanLoadSound(soundTag)) {
            return
        }

        val soundPool = getSoundPool(soundPoolName) ?: return

        CsLogger.tag(Config.TAG).i("start load. soundTag: $soundTag")

        synchronized(soundInfoMap) {
            val id = soundPool.load(CsContextManager.getApplication(), resId, 1)
            if (id <= 0) {
                CsLogger.tag(Config.TAG).i("load error. soundTag: $soundTag")
                return
            }

            val info = SoundInfo(soundPoolName)
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
        soundPoolName: String = Config.DEFAULT_POOL_NAME
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

        val soundPool = getSoundPool(soundPoolName) ?: return

        CsLogger.tag(Config.TAG).i("start load. soundTag: $soundTag")

        try {
            val afd = CsContextManager.getApplication().assets.openFd(fileName)

            synchronized(soundInfoMap) {
                val id = soundPool.load(afd, 1)
                if (id <= 0) {
                    CsLogger.tag(Config.TAG).i("load error. soundTag: $soundTag")
                    return
                }

                val info = SoundInfo(soundPoolName)
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
        val soundConfig = getSoundConfig(soundInfo.poolName) ?: return -1
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
        @FloatRange(from = 0.0, to = 1.0) leftVolume: Float,
        @FloatRange(from = 0.0, to = 1.0) rightVolume: Float,
        loop: Int,
        rate: Float
    ): Int {
        val soundInfo = getSoundInfo(soundTag) ?: return -1
        if (!soundInfo.isReady) {
            return -1
        }
        val soundPool = getSoundPool(soundInfo.poolName) ?: return -1

        val realLoop = if (loop < 0) {
            -1
        } else {
            loop
        }

        return soundPool.play(soundInfo.soundId, leftVolume, rightVolume, 0, realLoop, rate)
    }

    /**
     * 暂停播放
     *
     * @param playId    播放 id, 播放时返回的 id
     * */
    fun pause(playId: Int, soundPoolName: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolName) ?: return
        soundPool.pause(playId)
        CsLogger.tag(Config.TAG).i("pause success. playId: $playId")
    }

    /**
     * 暂停所有正在播放的音频
     * */
    fun pauseAll(soundPoolName: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolName) ?: return
        soundPool.autoPause()
        CsLogger.tag(Config.TAG).i("autoPause success.")
    }

    /**
     * 继续播放
     *
     * @param playId    播放 id, 播放时返回的 id
     * */
    fun resume(playId: Int, soundPoolName: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolName) ?: return
        soundPool.resume(playId)
        CsLogger.tag(Config.TAG).i("resume success. playId: $playId")
    }

    /**
     * 继续播放所有暂停的音频
     * */
    fun resumeAll(soundPoolName: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolName) ?: return
        soundPool.autoResume()
        CsLogger.tag(Config.TAG).i("autoResume success.")
    }

    /**
     * 结束播放
     *
     * @param playId    播放 id, 播放时返回的 id
     * */
    fun stop(playId: Int, soundPoolName: String = Config.DEFAULT_POOL_NAME) {
        val soundPool = getSoundPool(soundPoolName) ?: return
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
        val soundPool = getSoundPool(soundInfo.poolName) ?: return

        if (soundPool.unload(soundInfo.soundId)) {
            CsLogger.tag(Config.TAG).i("unload success. soundTag: $soundTag")
        }
    }

    /**
     * 释放目标音频池, 包括音频池内全部资源与音频池配置
     * */
    fun release(soundPoolName: String = Config.DEFAULT_POOL_NAME) {
        synchronized(soundPoolMap) {
            val soundPool = soundPoolMap.remove(soundPoolName) ?: return
            soundPool.release()
        }

        synchronized(soundConfigMap) {
            soundConfigMap.remove(soundPoolName)
        }

        synchronized(soundInfoMap) {
            val iterator = soundInfoMap.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.value.poolName == soundPoolName) {
                    iterator.remove()
                }
            }
        }

        CsLogger.tag(Config.TAG).i("release success. soundPoolName: $soundPoolName")
    }

    private class LoadCompleteListener(
        private val poolName: String,
        private val soundMap: HashMap<String, SoundInfo>
    ) : SoundPool.OnLoadCompleteListener {
        override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
            synchronized(soundMap) {
                for (entry in soundMap) {
                    val value = entry.value

                    if (value.poolName != poolName) {
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
                }
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
    private fun getSoundPool(soundPoolName: String): SoundPool? {
        val soundPool = soundPoolMap[soundPoolName]
        if (soundPool == null) {
            CsLogger.tag(Config.TAG)
                .e("The target soundPool has not been initialized. soundPoolName: $soundPoolName")
            return null
        }
        return soundPool
    }

    /**
     * 获取对应 SoundConfig
     * */
    private fun getSoundConfig(soundPoolName: String): SoundConfig? {
        val soundConfig = soundConfigMap[soundPoolName]
        if (soundConfig == null) {
            CsLogger.tag(Config.TAG)
                .e("The target soundPool has not been initialized. soundPoolName: $soundPoolName")
            return null
        }
        return soundConfig
    }
}