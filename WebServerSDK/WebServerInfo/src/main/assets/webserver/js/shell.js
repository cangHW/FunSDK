/**
 * WebServer Shell：侧边栏 + iframe 内嵌插件页 + hash 路由
 *
 * Hash 格式：/#/plugin/{pluginId}（前端 hash 路由，非 HTTP API）
 * 刷新后仍停留在当前插件。
 *
 * 依赖：api-routes.js（WebServerApiRoutes）
 */
window.WebServerShell = {
    HASH_PREFIX: '/plugin/',

    responseKeys() {
        return WebServerApiRoutes.Keys;
    },

    async request(url, options) {
        const keys = this.responseKeys();
        const response = await fetch(url, options);
        const json = await response.json();
        if (json[keys.CODE] !== WebServerApiRoutes.CODE_SUCCESS) {
            throw new Error(json[keys.MESSAGE] || ('request failed: ' + response.status));
        }
        return json;
    },

    async loadPlugins() {
        const keys = this.responseKeys();
        const json = await this.request(WebServerApiRoutes.PLUGIN_LIST);
        return (json[keys.DATA] && json[keys.DATA][keys.LIST]) ? json[keys.DATA][keys.LIST] : [];
    },

    buildHash(pluginId) {
        return '#/' + this.HASH_PREFIX.replace(/^\//, '') + encodeURIComponent(pluginId);
    },

    parseHashPluginId() {
        const hash = (location.hash || '').replace(/^#/, '');
        if (!hash.startsWith(this.HASH_PREFIX)) {
            return null;
        }
        const id = hash.substring(this.HASH_PREFIX.length);
        return id ? decodeURIComponent(id) : null;
    }
};

var cachedPlugins = [];
var activePluginId = null;

function getFrame() {
    return document.getElementById('plugin-frame');
}

function getWelcome() {
    return document.getElementById('welcome');
}

function findPlugin(pluginId) {
    var keys = WebServerApiRoutes.Keys;
    return cachedPlugins.find(function (p) {
        return p[keys.ID] === pluginId;
    });
}

function selectPlugin(pluginId, updateHash) {
    if (!pluginId) {
        showWelcome();
        return;
    }

    var plugin = findPlugin(pluginId);
    var keys = WebServerApiRoutes.Keys;
    if (!plugin || !plugin[keys.PAGE_URL]) {
        showWelcome();
        return;
    }

    activePluginId = pluginId;
    if (updateHash !== false) {
        var nextHash = WebServerShell.buildHash(pluginId);
        if (location.hash !== nextHash) {
            location.hash = nextHash;
        }
    }

    var frame = getFrame();
    var welcome = getWelcome();
    var pageUrl = plugin[keys.PAGE_URL];

    if (frame.getAttribute('data-plugin-id') !== pluginId) {
        frame.src = pageUrl;
        frame.setAttribute('data-plugin-id', pluginId);
    }

    frame.hidden = false;
    if (welcome) {
        welcome.hidden = true;
    }

    updateNavActive(pluginId);
}

function showWelcome() {
    activePluginId = null;
    var frame = getFrame();
    var welcome = getWelcome();

    if (frame) {
        frame.hidden = true;
        frame.removeAttribute('data-plugin-id');
        frame.src = 'about:blank';
    }
    if (welcome) {
        welcome.hidden = false;
    }
    updateNavActive(null);
}

function updateNavActive(pluginId) {
    var items = document.querySelectorAll('.plugin-nav-item');
    items.forEach(function (item) {
        var id = item.getAttribute('data-plugin-id');
        if (pluginId && id === pluginId) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
}

function renderSidebar(plugins) {
    var nav = document.getElementById('plugin-nav');
    var status = document.getElementById('status');
    if (!nav) {
        return;
    }

    cachedPlugins = plugins || [];

    if (!cachedPlugins.length) {
        nav.innerHTML = '<div class="empty">暂无已注册插件</div>';
        if (status) {
            status.textContent = '';
        }
        showWelcome();
        return;
    }

    nav.innerHTML = cachedPlugins.map(function (plugin) {
        var keys = WebServerApiRoutes.Keys;
        var id = plugin[keys.ID] || '';
        var title = plugin[keys.TITLE] || id;
        return '<button type="button" class="plugin-nav-item" data-plugin-id="' + escapeHtml(id) + '">' +
            '<div class="plugin-title">' + escapeHtml(title) + '</div>' +
            '<div class="plugin-id">' + escapeHtml(id) + '</div>' +
            '</button>';
    }).join('');

    nav.querySelectorAll('.plugin-nav-item').forEach(function (btn) {
        btn.addEventListener('click', function () {
            selectPlugin(btn.getAttribute('data-plugin-id'));
        });
    });

    if (status) {
        status.textContent = '已刷新 · ' + new Date().toLocaleTimeString();
    }

    var hashId = WebServerShell.parseHashPluginId();
    if (hashId) {
        selectPlugin(hashId, false);
    } else if (activePluginId) {
        selectPlugin(activePluginId, false);
    }
}

async function refreshPlugins() {
    var nav = document.getElementById('plugin-nav');
    try {
        var plugins = await WebServerShell.loadPlugins();
        renderSidebar(plugins);
    } catch (error) {
        if (nav) {
            nav.innerHTML = '<div class="error">加载失败：' + escapeHtml(error.message) + '</div>';
        }
        var status = document.getElementById('status');
        if (status) {
            status.textContent = '';
        }
    }
}

function escapeHtml(text) {
    return String(text)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function onHashChange() {
    var pluginId = WebServerShell.parseHashPluginId();
    if (pluginId) {
        selectPlugin(pluginId, false);
    } else {
        showWelcome();
    }
}

document.addEventListener('DOMContentLoaded', function () {
    window.addEventListener('hashchange', onHashChange);
    refreshPlugins();
    setInterval(refreshPlugins, 3000);
});
