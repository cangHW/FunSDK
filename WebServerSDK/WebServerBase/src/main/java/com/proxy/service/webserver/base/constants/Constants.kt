package com.proxy.service.webserver.base.constants

/**
 * @author: cangHX
 * @date: 2026/6/15 17:20
 * @desc:
 */
object Constants {

    const val LOG_TAG_START = "CsWebServer_"

    const val MIN_PORT = 1024L
    const val MAX_PORT = 65535L
    const val DEFAULT_PORT = 9527

    /**
     * web 服务端根目录
     * */
    const val SHELL_ASSET_ROOT = "webserver"

    /**
     * web 服务端插件根目录
     * */
    const val PLUGIN_ASSET_ROOT = "webserver/plugins"

    /**
     * 主页 html 名称
     * */
    const val PAGE_INDEX_HTML = "index.html"

    /*** *** *** *** *** *** 静态资源 URL *** *** *** *** *** ***/

    /**
     * 静态资源根目录
     * */
    const val API_STATIC_ROOT_PATH = "/static/"

    /**
     * shell 静态资源链接: /static/res/{relativePath}
     * */
    const val API_STATIC_RES_PATH = "${API_STATIC_ROOT_PATH}res/"

    /**
     * 插件静态资源链接: /static/plugin/{pluginId}/{relativePath}
     * */
    const val API_STATIC_PLUGIN_PATH = "${API_STATIC_ROOT_PATH}plugin/"

    /*** *** *** *** *** *** 插件通用接口 URL *** *** *** *** *** ***/

    /**
     * 插件通用接口根目录
     * */
    const val API_PLUGIN_ROOT_PATH = "/plugin/"

    /**
     * 获取全部插件信息接口: /plugin/list
     * */
    const val API_PLUGIN_LIST_URL = "${API_PLUGIN_ROOT_PATH}list"

    /**
     * 更新对应插件配置接口: /plugin/{pluginId}/config
     * */
    const val API_PLUGIN_CONFIG_ACTION = "config"

    /**
     * 请求对应插件数据接口: /plugin/{pluginId}/request
     * */
    const val API_PLUGIN_REQUEST_ACTION = "request"

    /*** *** *** *** *** *** 插件自定义接口 URL *** *** *** *** *** ***/

    /***
     * 插件自定义接口根目录.
     * 插件接口 /custom/${plugin_id}/...
     * */
    const val API_CUSTOM_ROOT_PATH = "/custom/"
}