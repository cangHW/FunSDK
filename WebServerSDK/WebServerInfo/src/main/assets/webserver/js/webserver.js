/**
 * WebServer 公共 API（插件 HTML 可引用）
 *
 * 依赖：api-routes.js（WebServerApiRoutes）
 *
 * 推荐（Shell 公共资源，所有插件共用）：
 *   <script src="/static/res/js/api-routes.js"></script>
 *   <script src="/static/res/js/webserver.js"></script>
 *
 * 插件私有资源（需在各插件模块自己放置文件）：
 *   <script src="/static/plugin/{pluginId}/js/webserver.js"></script>
 */
window.WebServer = {
    pluginId: null,

    responseKeys() {
        return WebServerApiRoutes.Keys;
    },

    init(pluginId) {
        this.pluginId = pluginId;
    },

    async request(url, options) {
        const keys = this.responseKeys();
        const response = await fetch(url, options || {});
        const json = await response.json();
        if (json[keys.CODE] !== WebServerApiRoutes.CODE_SUCCESS) {
            throw new Error(json[keys.MESSAGE] || ('request failed: ' + response.status));
        }
        return json;
    },

    async loadData(pluginId) {
        const keys = this.responseKeys();
        const id = pluginId || this.pluginId;
        const json = await this.request(WebServerApiRoutes.pluginRequestUrl(id));
        return json[keys.DATA] || {};
    },

    async updateConfig(pluginId, config) {
        const keys = this.responseKeys();
        const id = pluginId || this.pluginId;
        const body = typeof config === 'string' ? config : JSON.stringify(config);
        return this.request(WebServerApiRoutes.pluginConfigUrl(id), {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: keys.CONFIG + '=' + encodeURIComponent(body)
        });
    },

    /**
     * 调用插件自定义 API（/custom/{pluginId}/{subPath}）
     *
     * @param subPath  自定义子路径，如 session/list
     * @param options  fetch 选项（method、headers、body 等）
     * @returns JSON 或纯文本，按 Content-Type 自动解析
     */
    async requestCustom(subPath, options) {
        const id = this.pluginId;
        if (!id) {
            throw new Error('pluginId not set, call init(pluginId) first');
        }
        const url = WebServerApiRoutes.customUrl(id, subPath);
        const response = await fetch(url, options || {});
        if (!response.ok) {
            throw new Error('request failed: ' + response.status);
        }
        const contentType = response.headers.get('Content-Type') || '';
        if (contentType.indexOf('application/json') >= 0) {
            return response.json();
        }
        return response.text();
    },

    prettyJson(value) {
        if (typeof value === 'string') {
            try {
                return JSON.stringify(JSON.parse(value), null, 2);
            } catch (e) {
                return value;
            }
        }
        return JSON.stringify(value, null, 2);
    }
};
