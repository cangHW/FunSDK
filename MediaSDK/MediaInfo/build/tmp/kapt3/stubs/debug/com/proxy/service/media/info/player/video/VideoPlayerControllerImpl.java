package com.proxy.service.media.info.player.video;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/2/18 10:59
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0018\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0010\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0012H\u0016\u00a8\u0006\u0019"}, d2 = {"Lcom/proxy/service/media/info/player/video/VideoPlayerControllerImpl;", "Lcom/proxy/service/media/base/controller/player/video/IVideoPlayerController;", "player", "Landroidx/media3/exoplayer/ExoPlayer;", "(Landroidx/media3/exoplayer/ExoPlayer;)V", "addMediaSource", "Ljava/lang/Void;", "source", "Lcom/proxy/service/media/base/config/MediaSource;", "seekTo", "", "positionMs", "", "setLifecycleOwner", "owner", "Landroidx/lifecycle/LifecycleOwner;", "setPlaybackParameters", "speed", "", "pitch", "setRepeatMode", "repeatMode", "Lcom/proxy/service/media/base/enums/RepeatMode;", "setVolume", "volume", "MediaInfo_debug"})
public final class VideoPlayerControllerImpl implements com.proxy.service.media.base.controller.player.video.IVideoPlayerController {
    
    public VideoPlayerControllerImpl(@org.jetbrains.annotations.NotNull
    androidx.media3.exoplayer.ExoPlayer player) {
        super();
    }
    
    @java.lang.Override
    public void seekTo(long positionMs) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.Void setPlaybackParameters(float speed, float pitch) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.Void setVolume(float volume) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.Void setRepeatMode(@org.jetbrains.annotations.NotNull
    com.proxy.service.media.base.enums.RepeatMode repeatMode) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.Void addMediaSource(@org.jetbrains.annotations.NotNull
    com.proxy.service.media.base.config.MediaSource source) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.Void setLifecycleOwner(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner owner) {
        return null;
    }
}