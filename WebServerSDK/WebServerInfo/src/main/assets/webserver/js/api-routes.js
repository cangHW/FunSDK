/**
 * Web API 路径与响应字段（与 Constants.kt / ApiResponseKeys 保持一致）
 */
window.WebServerApiRoutes = {
    PLUGIN_ROOT: '/plugin/',
    PLUGIN_LIST: '/plugin/list',
    CUSTOM_ROOT: '/custom/',

    pluginRequestUrl(pluginId) {
        return this.PLUGIN_ROOT + encodeURIComponent(pluginId) + '/request';
    },

    pluginConfigUrl(pluginId) {
        return this.PLUGIN_ROOT + encodeURIComponent(pluginId) + '/config';
    },

    /**
     * 构建插件自定义 API URL：/custom/{pluginId}/{subPath}
     *
     * @param pluginId 插件 ID
     * @param subPath  自定义子路径，如 session/list
     */
    customUrl(pluginId, subPath) {
        var id = encodeURIComponent(pluginId);
        var clean = String(subPath || '').replace(/^\/+/, '');
        if (!clean) {
            return this.CUSTOM_ROOT + id;
        }
        var encodedPath = clean.split('/').map(function (segment) {
            return encodeURIComponent(segment);
        }).join('/');
        return this.CUSTOM_ROOT + id + '/' + encodedPath;
    },

    Keys: {
        CODE: 'code',
        MESSAGE: 'message',
        DATA: 'data',
        LIST: 'list',
        ID: 'id',
        TITLE: 'title',
        PAGE_URL: 'pageUrl',
        CONFIG: 'config'
    },

    CODE_SUCCESS: 0
};
