/**
 * DebugBridge 公共 API，供各插件 HTML 调用
 */
window.DebugBridge = {
    getSessionId() {
        const parts = location.pathname.split('/').filter(Boolean);
        const index = parts.indexOf('session');
        if (index >= 0 && parts[index + 1]) {
            return parts[index + 1];
        }
        return new URLSearchParams(location.search).get('sessionId');
    },

    async fetchDetail(sessionId) {
        const id = sessionId || this.getSessionId();
        const response = await fetch('/api/session/' + id + '/detail');
        if (!response.ok) {
            throw new Error('load session failed: ' + response.status);
        }
        return response.json();
    },

    async decide(sessionId, payload) {
        const id = sessionId || this.getSessionId();
        const response = await fetch('/api/session/' + id + '/decide', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            throw new Error('decide failed: ' + response.status);
        }
        return response.json();
    },

    prettyJson(text) {
        try {
            return JSON.stringify(JSON.parse(text), null, 2);
        } catch (e) {
            return text;
        }
    }
};
