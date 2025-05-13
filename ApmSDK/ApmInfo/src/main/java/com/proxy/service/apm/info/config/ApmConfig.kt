package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.Controller

/**
 * @author: cangHX
 * @data: 2025/4/22 17:39
 * @desc:
 */
class ApmConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun getRootDir(): String {
        return builder.getRootDir()
    }

    override fun getJavaCrashMonitorController(): Controller {
        return builder.getJavaCrashMonitorController()
    }

    override fun getMainThreadLagMonitorController(): Controller {
        return builder.getMainThreadLagMonitorController()
    }

    override fun getUiLagMonitorController(): Controller {
        return builder.getUiLagMonitorController()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var rootDir = ""
        private var javaCrashMonitorController: Controller = Controller.builder().build()
        private var mainThreadLagMonitorController: Controller = Controller.builder().build()
        private var uiLagMonitorController: Controller = Controller.builder().build()

        override fun setRootDir(rootDir: String): IBuilder {
            this.rootDir = rootDir
            return this
        }

        override fun setJavaCrashMonitorController(controller: Controller): IBuilder {
            this.javaCrashMonitorController = controller
            return this
        }

        override fun setMainThreadLagMonitorEnable(controller: Controller): IBuilder {
            this.mainThreadLagMonitorController = controller
            return this
        }

        override fun setUiLagMonitorEnable(controller: Controller): IBuilder {
            this.uiLagMonitorController = controller
            return this
        }

        override fun build(): ApmConfig {
            return ApmConfig(this)
        }

        override fun getRootDir(): String {
            return rootDir
        }

        override fun getJavaCrashMonitorController(): Controller {
            return javaCrashMonitorController
        }

        override fun getMainThreadLagMonitorController(): Controller {
            return mainThreadLagMonitorController
        }

        override fun getUiLagMonitorController(): Controller {
            return uiLagMonitorController
        }
    }

}