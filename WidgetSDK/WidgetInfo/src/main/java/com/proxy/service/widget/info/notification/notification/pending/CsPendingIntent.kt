package com.proxy.service.widget.info.notification.notification.pending

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2025/12/16 14:34
 * @desc:
 */
class CsPendingIntent private constructor(
    private val type: Int,
    private val requestCode: Int,
    private val intents: MutableList<Intent>
) {

    companion object {
        private const val TYPE_ACTIVITY = 0
        private const val TYPE_ACTIVITYS = 1
        private const val TYPE_SERVICE = 10
        private const val TYPE_FOREGROUND_SERVICE = 11
        private const val TYPE_BROADCAST = 20

        /**
         * 启动 activity
         * */
        fun launchActivity(intent: Intent): CsPendingIntent {
            return launchActivity(System.currentTimeMillis().toInt(), intent)
        }

        /**
         * 启动 activity
         * */
        fun launchActivity(requestCode: Int, intent: Intent): CsPendingIntent {
            return CsPendingIntent(TYPE_ACTIVITY, requestCode, arrayListOf(intent))
        }

        /**
         * 启动 activity
         * */
        fun launchActivitys(intents: MutableList<Intent>): CsPendingIntent {
            return launchActivitys(System.currentTimeMillis().toInt(), intents)
        }

        /**
         * 启动 activity
         * */
        fun launchActivitys(requestCode: Int, intents: MutableList<Intent>): CsPendingIntent {
            return CsPendingIntent(TYPE_ACTIVITYS, requestCode, intents)
        }

        /**
         * 启动 service
         * */
        fun launchService(intent: Intent): CsPendingIntent {
            return launchService(System.currentTimeMillis().toInt(), intent)
        }

        /**
         * 启动 service
         * */
        fun launchService(requestCode: Int, intent: Intent): CsPendingIntent {
            return CsPendingIntent(TYPE_SERVICE, requestCode, arrayListOf(intent))
        }

        /**
         * 启动 foreground service
         * */
        @RequiresApi(Build.VERSION_CODES.O)
        fun launchForegroundService(intent: Intent): CsPendingIntent {
            return launchForegroundService(System.currentTimeMillis().toInt(), intent)
        }

        /**
         * 启动 foreground service
         * */
        @RequiresApi(Build.VERSION_CODES.O)
        fun launchForegroundService(requestCode: Int, intent: Intent): CsPendingIntent {
            return CsPendingIntent(TYPE_FOREGROUND_SERVICE, requestCode, arrayListOf(intent))
        }

        /**
         * 启动 broadcast
         * */
        fun launchBroadcast(intent: Intent): CsPendingIntent {
            return launchBroadcast(System.currentTimeMillis().toInt(), intent)
        }

        /**
         * 启动 broadcast
         * */
        fun launchBroadcast(requestCode: Int, intent: Intent): CsPendingIntent {
            return CsPendingIntent(TYPE_BROADCAST, requestCode, arrayListOf(intent))
        }
    }


    fun getPendingIntent(): PendingIntent? {
        if (intents.isEmpty()) {
            return null
        }
        when (type) {
            TYPE_ACTIVITY -> {
                return PendingIntent.getActivity(
                    CsContextManager.getApplication(),
                    System.currentTimeMillis().toInt(),
                    intents.first(),
                    PendingIntent.FLAG_MUTABLE
                )
            }

            TYPE_ACTIVITYS -> {
                return PendingIntent.getActivities(
                    CsContextManager.getApplication(),
                    System.currentTimeMillis().toInt(),
                    intents.toTypedArray(),
                    PendingIntent.FLAG_MUTABLE
                )
            }

            TYPE_SERVICE -> {
                return PendingIntent.getService(
                    CsContextManager.getApplication(),
                    System.currentTimeMillis().toInt(),
                    intents.first(),
                    PendingIntent.FLAG_MUTABLE
                )
            }

            TYPE_FOREGROUND_SERVICE -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    return null
                }
                return PendingIntent.getForegroundService(
                    CsContextManager.getApplication(),
                    System.currentTimeMillis().toInt(),
                    intents.first(),
                    PendingIntent.FLAG_MUTABLE
                )
            }

            TYPE_BROADCAST -> {
                return PendingIntent.getBroadcast(
                    CsContextManager.getApplication(),
                    System.currentTimeMillis().toInt(),
                    intents.first(),
                    PendingIntent.FLAG_MUTABLE
                )
            }
        }
        return null
    }

}