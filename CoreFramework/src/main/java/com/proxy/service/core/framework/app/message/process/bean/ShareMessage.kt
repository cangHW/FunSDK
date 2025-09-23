package com.proxy.service.core.framework.app.message.process.bean

import android.os.Parcel
import android.os.Parcelable
import android.os.Process
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.message.process.channel.ChannelEnum
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2025/9/15 20:58
 * @desc:
 */
class ShareMessage : Parcelable {

    /**
     * 消息 id, 用于记录发送、响应等过程
     * */
    val messageId: String

    /**
     * 消息版本, 用于标识同一功能的不同版本处理
     * */
    val messageVersion: String

    /**
     * 消息类型
     * */
    val messageType: String

    /**
     * 消息接收渠道
     * */
    val receiveChannel: String

    /**
     * 消息发送时间
     * */
    val messageTime: Long

    /**
     * 调用功能名称
     * */
    val method: String

    /**
     * 数据内容
     * */
    val content: String

    constructor(
        messageId: String,
        messageVersion: String,
        messageType: String,
        receiveChannel: String,
        messageTime: Long,
        method: String,
        content: String
    ) {
        this.messageId = messageId
        this.messageVersion = messageVersion
        this.messageType = messageType
        this.receiveChannel = receiveChannel
        this.messageTime = messageTime
        this.method = method
        this.content = content
    }

    constructor(parcel: Parcel) {
        messageId = parcel.readString() ?: ""
        messageVersion = parcel.readString() ?: ""
        messageType = parcel.readString() ?: ""
        receiveChannel = parcel.readString() ?: ""
        messageTime = parcel.readLong()
        method = parcel.readString() ?: ""
        content = parcel.readString() ?: ""
    }

    fun isRequest(): Boolean {
        if (ShareMessageFactory.DEFAULT_TYPE_REQUEST_SYNC == messageType) {
            return true
        }
        return ShareMessageFactory.DEFAULT_TYPE_REQUEST_ASYNC == messageType
    }

    fun isResponse(): Boolean {
        if (ShareMessageFactory.DEFAULT_TYPE_RESPONSE_WAITING == messageType) {
            return true
        }
        if (ShareMessageFactory.DEFAULT_TYPE_RESPONSE_PROGRESS == messageType) {
            return true
        }
        return ShareMessageFactory.DEFAULT_TYPE_RESPONSE_FINISH == messageType
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(messageId)
        dest.writeString(messageVersion)
        dest.writeString(messageType)
        dest.writeString(receiveChannel)
        dest.writeLong(messageTime)
        dest.writeString(method)
        dest.writeString(content)
    }

    override fun toString(): String {
        return "ShareMessage(messageId='$messageId', messageVersion='$messageVersion', messageType='$messageType', receiveChannel='$receiveChannel', messageTime=$messageTime, method='$method', content='$content')"
    }

    companion object CREATOR : Parcelable.Creator<ShareMessage> {
        override fun createFromParcel(parcel: Parcel): ShareMessage {
            return ShareMessage(parcel)
        }

        override fun newArray(size: Int): Array<ShareMessage?> {
            return arrayOfNulls(size)
        }
    }
}

object ShareMessageFactory {

    private val Ids: AtomicInteger = AtomicInteger(0)
    const val DEFAULT_VERSION: String = "V1"

    const val DEFAULT_TYPE_REQUEST_SYNC: String = "request_sync"
    const val DEFAULT_TYPE_REQUEST_ASYNC: String = "request_async"

    const val DEFAULT_TYPE_RESPONSE_WAITING: String = "response_waiting"
    const val DEFAULT_TYPE_RESPONSE_PROGRESS: String = "response_progress"
    const val DEFAULT_TYPE_RESPONSE_FINISH: String = "response_finish"
    const val DEFAULT_TYPE_RESPONSE_ERROR: String = "response_error"

    /**
     * 创建消息 id
     * */
    private fun createMessageId(): String {
        return CsAppUtils.getPackageName() + "_" + Process.myPid() + "_" + Ids.getAndIncrement()
    }

    /**
     * 创建同步请求
     * */
    fun createRequestSync(
        messageVersion: String,
        receiveChannel: String,
        method: String,
        params: String
    ): ShareMessage {
        return ShareMessage(
            createMessageId(),
            messageVersion,
            DEFAULT_TYPE_REQUEST_SYNC,
            receiveChannel,
            System.currentTimeMillis(),
            method,
            params
        )
    }

    /**
     * 创建异步请求
     * */
    fun createRequestAsync(
        messageVersion: String,
        receiveChannel: String,
        method: String,
        params: String
    ): ShareMessage {
        return ShareMessage(
            createMessageId(),
            messageVersion,
            DEFAULT_TYPE_REQUEST_ASYNC,
            receiveChannel,
            System.currentTimeMillis(),
            method,
            params
        )
    }


    /**
     * 创建错误返回
     * */
    fun createResponseError(request: ShareMessage, errorCode: Int): ShareMessage {
        return ShareMessage(
            request.messageId,
            request.messageVersion,
            DEFAULT_TYPE_RESPONSE_ERROR,
            ChannelEnum.NONE.name,
            System.currentTimeMillis(),
            request.method,
            "$errorCode"
        )
    }

    /**
     * 创建等待返回
     * */
    fun createResponseWaiting(request: ShareMessage): ShareMessage {
        return ShareMessage(
            request.messageId,
            request.messageVersion,
            DEFAULT_TYPE_RESPONSE_WAITING,
            ChannelEnum.NONE.name,
            System.currentTimeMillis(),
            request.method,
            ""
        )
    }

    /**
     * 创建进度返回
     * */
    fun createResponseProgress(
        messageVersion: String,
        request: ShareMessage,
        response: String
    ): ShareMessage {
        return ShareMessage(
            request.messageId,
            messageVersion,
            DEFAULT_TYPE_RESPONSE_PROGRESS,
            ChannelEnum.NONE.name,
            System.currentTimeMillis(),
            request.method,
            response
        )
    }

    /**
     * 创建结束返回
     * */
    fun createResponseFinish(
        messageVersion: String,
        request: ShareMessage,
        response: String
    ): ShareMessage {
        return ShareMessage(
            request.messageId,
            messageVersion,
            DEFAULT_TYPE_RESPONSE_FINISH,
            ChannelEnum.NONE.name,
            System.currentTimeMillis(),
            request.method,
            response
        )
    }
}
