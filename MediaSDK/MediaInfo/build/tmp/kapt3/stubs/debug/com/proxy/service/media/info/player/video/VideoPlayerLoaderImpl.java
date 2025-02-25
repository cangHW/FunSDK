package com.proxy.service.media.info.player.video;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/2/18 09:57
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\rH\u0016J\u0010\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0006H\u0016J\u0018\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u001f\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010 \u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010!\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\"\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010#\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010$\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010%\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010&\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\bH\u0016J\u0010\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020*H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/proxy/service/media/info/player/video/VideoPlayerLoaderImpl;", "Lcom/proxy/service/media/base/loader/player/video/IVideoPlayerLoader;", "()V", "callback", "Lcom/proxy/service/media/base/callback/MediaPlayerStatusCallback;", "lifecycleOwner", "Landroidx/lifecycle/LifecycleOwner;", "pitch", "", "repeatMode", "Lcom/proxy/service/media/base/enums/RepeatMode;", "sources", "Ljava/util/ArrayList;", "Lcom/proxy/service/media/base/config/MediaSource;", "speed", "surface", "Landroid/view/Surface;", "surfaceHolder", "Landroid/view/SurfaceHolder;", "surfaceView", "Landroid/view/SurfaceView;", "textureView", "Landroid/view/TextureView;", "viewGroup", "Landroid/view/ViewGroup;", "volume", "addMediaSource", "source", "setLifecycleOwner", "owner", "setPlaybackParameters", "setRepeatMode", "setStatusCallback", "setVideoSurface", "setVideoSurfaceHolder", "setVideoSurfaceView", "setVideoTextureView", "setVideoViewGroup", "setVolume", "startAsync", "Lcom/proxy/service/media/base/controller/player/video/IVideoPlayerController;", "autoPlay", "", "MediaInfo_debug"})
public final class VideoPlayerLoaderImpl implements com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader {
    private float speed = 1.0F;
    private float pitch = 1.0F;
    private float volume = 1.0F;
    private com.proxy.service.media.base.enums.RepeatMode repeatMode;
    private final java.util.ArrayList<com.proxy.service.media.base.config.MediaSource> sources = null;
    private androidx.lifecycle.LifecycleOwner lifecycleOwner;
    private android.view.Surface surface;
    private android.view.SurfaceHolder surfaceHolder;
    private android.view.SurfaceView surfaceView;
    private android.view.TextureView textureView;
    private android.view.ViewGroup viewGroup;
    private com.proxy.service.media.base.callback.MediaPlayerStatusCallback callback;
    
    public VideoPlayerLoaderImpl() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setPlaybackParameters(float speed, float pitch) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setVolume(float volume) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setRepeatMode(@org.jetbrains.annotations.NotNull
    com.proxy.service.media.base.enums.RepeatMode repeatMode) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader addMediaSource(@org.jetbrains.annotations.NotNull
    com.proxy.service.media.base.config.MediaSource source) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setLifecycleOwner(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner owner) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setVideoSurface(@org.jetbrains.annotations.NotNull
    android.view.Surface surface) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setVideoSurfaceHolder(@org.jetbrains.annotations.NotNull
    android.view.SurfaceHolder surfaceHolder) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setVideoSurfaceView(@org.jetbrains.annotations.NotNull
    android.view.SurfaceView surfaceView) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setVideoTextureView(@org.jetbrains.annotations.NotNull
    android.view.TextureView textureView) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setVideoViewGroup(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup viewGroup) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.loader.player.video.IVideoPlayerLoader setStatusCallback(@org.jetbrains.annotations.NotNull
    com.proxy.service.media.base.callback.MediaPlayerStatusCallback callback) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.proxy.service.media.base.controller.player.video.IVideoPlayerController startAsync(boolean autoPlay) {
        return null;
    }
}