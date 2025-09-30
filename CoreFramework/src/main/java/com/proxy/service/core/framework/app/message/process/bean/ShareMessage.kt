package com.proxy.service.core.framework.app.message.process.bean

import android.os.Parcel
import android.os.Parcelable
import android.os.Process
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.message.process.channel.ReceiveChannel
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
    private val _messageType: String

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
        this._messageType = messageType
        this.receiveChannel = receiveChannel
        this.messageTime = messageTime
        this.method = method
        this.content = content
    }

    constructor(parcel: Parcel) {
        messageId = parcel.readString() ?: ""
        messageVersion = parcel.readString() ?: ""
        _messageType = parcel.readString() ?: ""
        receiveChannel = parcel.readString() ?: ""
        messageTime = parcel.readLong()
        method = parcel.readString() ?: ""
        content = parcel.readString() ?: ""
    }

    /**
     * 获取消息类型
     * */
    fun getMessageType(): MessageType {
        return MessageType.valueOf(_messageType)
    }

    /**
     * 是否请求消息
     * */
    fun isRequest(): Boolean {
        return when (_messageType) {
            MessageType.REQUEST_SYNC.name, MessageType.REQUEST_ASYNC.name -> {
                true
            }

            else -> {
                false
            }
        }
    }

    /**
     * 是否响应消息
     * */
    fun isResponse(): Boolean {
        return when (_messageType) {
            MessageType.RESPONSE_WAITING.name,
            MessageType.RESPONSE_PROGRESS.name,
            MessageType.RESPONSE_FINISH.name,
            MessageType.RESPONSE_ERROR.name -> {
                true
            }

            else -> {
                false
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(messageId)
        dest.writeString(messageVersion)
        dest.writeString(_messageType)
        dest.writeString(receiveChannel)
        dest.writeLong(messageTime)
        dest.writeString(method)
        dest.writeString(content)
    }

    override fun toString(): String {
        return "ShareMessage(messageId='$messageId', messageVersion='$messageVersion', messageType='$_messageType', receiveChannel='$receiveChannel', messageTime=$messageTime, method='$method', content='$content')"
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
            MessageType.REQUEST_SYNC.name,
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
            MessageType.REQUEST_ASYNC.name,
            receiveChannel,
            System.currentTimeMillis(),
            method,
            params
        )
    }


    /**
     * 创建等待返回
     * */
    fun createResponseWaiting(request: ShareMessage): ShareMessage {
        return ShareMessage(
            request.messageId,
            request.messageVersion,
            MessageType.RESPONSE_WAITING.name,
            ReceiveChannel.NONE.name,
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
            MessageType.RESPONSE_PROGRESS.name,
            ReceiveChannel.NONE.name,
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
            MessageType.RESPONSE_FINISH.name,
            ReceiveChannel.NONE.name,
            System.currentTimeMillis(),
            request.method,
            response
        )
    }

    /**
     * 创建错误返回
     * */
    fun createResponseError(request: ShareMessage, errorCode: String): ShareMessage {
        return ShareMessage(
            request.messageId,
            request.messageVersion,
            MessageType.RESPONSE_ERROR.name,
            ReceiveChannel.NONE.name,
            System.currentTimeMillis(),
            request.method,
            errorCode
        )
    }
}
