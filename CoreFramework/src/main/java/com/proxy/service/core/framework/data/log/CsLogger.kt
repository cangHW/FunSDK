package com.proxy.service.core.framework.data.log

import timber.log.Timber

/**
 * 日志工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:26
 * @desc:
 */
object CsLogger : IL {

    private val explicitTag: ThreadLocal<String> = ThreadLocal()

    /**
     * 设置日志回调
     * */
    fun addLogCallback(callback: LogCallback) {
        LogInit.addLogCallback(callback)
    }

    /**
     * 设置是否允许打印到控制台，默认允许
     * */
    fun setPrintEnable(enable: Boolean) {
        LogInit.setPrintEnable(enable)
    }

    fun tag(tag: String): IL {
        explicitTag.set(tag)
        return this
    }

    override fun d(throwable: Throwable) {
        getTag()?.let {
            Timber.tag(it).d(throwable)
        } ?: let {
            Timber.d(throwable)
        }
    }

    override fun d(message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).d(message, args)
            } else {
                Timber.tag(it).d(message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.d(message, args)
            } else {
                Timber.d(message)
            }
        }
    }

    override fun d(throwable: Throwable, message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).d(throwable, message, args)
            } else {
                Timber.tag(it).d(throwable, message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.d(throwable, message, args)
            } else {
                Timber.d(throwable, message)
            }
        }
    }

    override fun e(throwable: Throwable) {
        getTag()?.let {
            Timber.tag(it).e(throwable)
        } ?: let {
            Timber.e(throwable)
        }
    }

    override fun e(message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).e(message, args)
            } else {
                Timber.tag(it).e(message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.e(message, args)
            } else {
                Timber.e(message)
            }
        }
    }

    override fun e(throwable: Throwable, message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).e(throwable, message, args)
            } else {
                Timber.tag(it).e(throwable, message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.e(throwable, message, args)
            } else {
                Timber.e(throwable, message)
            }
        }
    }

    override fun i(throwable: Throwable) {
        getTag()?.let {
            Timber.tag(it).i(throwable)
        } ?: let {
            Timber.i(throwable)
        }
    }

    override fun i(message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).i(message, args)
            } else {
                Timber.tag(it).i(message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.i(message, args)
            } else {
                Timber.i(message)
            }
        }
    }

    override fun i(throwable: Throwable, message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).i(throwable, message, args)
            } else {
                Timber.tag(it).i(throwable, message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.i(throwable, message, args)
            } else {
                Timber.i(throwable, message)
            }
        }
    }

    override fun v(throwable: Throwable) {
        getTag()?.let {
            Timber.tag(it).v(throwable)
        } ?: let {
            Timber.v(throwable)
        }
    }

    override fun v(message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).v(message, args)
            } else {
                Timber.tag(it).v(message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.v(message, args)
            } else {
                Timber.v(message)
            }
        }
    }

    override fun v(throwable: Throwable, message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).v(throwable, message, args)
            } else {
                Timber.tag(it).v(throwable, message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.v(throwable, message, args)
            } else {
                Timber.v(throwable, message)
            }
        }
    }

    override fun w(throwable: Throwable) {
        getTag()?.let {
            Timber.tag(it).w(throwable)
        } ?: let {
            Timber.w(throwable)
        }
    }

    override fun w(message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).w(message, args)
            } else {
                Timber.tag(it).w(message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.w(message, args)
            } else {
                Timber.w(message)
            }
        }
    }

    override fun w(throwable: Throwable, message: String, vararg args: Any) {
        getTag()?.let {
            if (args.isNotEmpty()) {
                Timber.tag(it).w(throwable, message, args)
            } else {
                Timber.tag(it).w(throwable, message)
            }
        } ?: let {
            if (args.isNotEmpty()) {
                Timber.w(throwable, message, args)
            } else {
                Timber.w(throwable, message)
            }
        }
    }

    private fun getTag(): String? {
        val tag = explicitTag.get()
        if (tag != null) {
            explicitTag.remove()
        }
        return tag
    }

}