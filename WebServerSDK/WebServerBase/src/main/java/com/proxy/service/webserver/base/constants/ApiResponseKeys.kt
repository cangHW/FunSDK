package com.proxy.service.webserver.base.constants

/**
 * HTTP JSON 响应字段与插件 API 契约字段
 *
 * 与 assets `api-routes.js` 中 [WebServerApiRoutes.Keys] 保持一致。
 */
object ApiResponseKeys {

    const val MIME_JSON = "application/json; charset=utf-8"

    const val CODE = "code"
    const val MESSAGE = "message"
    const val DATA = "data"

    const val CODE_SUCCESS = 0
    const val CODE_ERROR = -1

    /** GET /plugin/list → data.list[] */
    const val LIST = "list"
    const val ID = "id"
    const val TITLE = "title"
    const val PAGE_URL = "pageUrl"

    /** POST /plugin/{id}/config 表单字段 */
    const val CONFIG = "config"
}
