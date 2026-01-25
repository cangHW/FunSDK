package com.proxy.service.webview.monitor.work.performance

/**
 * @author: cangHX
 * @data: 2026/1/25 20:46
 * @desc:
 */
class PerformanceData {

    /**
     * 描述: 用户开始导航到当前页面的时间点。
     * 触发时机:
     *  1.如果是从一个页面跳转到另一个页面，则是用户点击链接或提交表单的时间。
     *  2.如果是直接输入 URL 或刷新页面，则是浏览器开始加载页面的时间。
     * 作用: 作为页面加载的起点，用于计算其他时间点的相对时间。
     * */
    var navigationStart: Long = 0L

    /**
     * 描述: 浏览器开始进行 DNS 查询的时间点。
     * 触发时机: 浏览器需要解析域名（如 example.com）为 IP 地址时触发。
     * 作用: 用于计算 DNS 查询的耗时。
     * */
    var domainLookupStart: Long = 0L

    /**
     * 描述: 浏览器完成 DNS 查询的时间点。
     * 触发时机: DNS 查询完成后触发。
     * 作用:
     *  与 domainLookupStart 的差值表示 DNS 查询的耗时。
     *  如果使用了 DNS 缓存，则 domainLookupStart 和 domainLookupEnd 的值相同。
     * */
    var domainLookupEnd: Long = 0L

    /**
     * 描述: 浏览器开始与服务器建立 TCP 连接的时间点。
     * 触发时机: 浏览器开始与服务器建立网络连接时触发。
     * 作用:
     *  用于计算 TCP 连接的耗时。
     *  如果使用了持久连接（如 HTTP/2 或复用的 TCP 连接），则 connectStart 和 connectEnd 的值可能相同。
     * */
    var connectStart: Long = 0L

    /**
     * 描述: 浏览器完成 TCP 连接的时间点。
     * 触发时机: TCP 连接建立完成后触发。
     * 作用:
     *  与 connectStart 的差值表示 TCP 连接的耗时。
     *  如果使用了 HTTPS，则包括 SSL/TLS 握手的时间。
     * */
    var connectEnd: Long = 0L

    /**
     * 描述: 浏览器开始向服务器发送 HTTP 请求的时间点。
     * 触发时机: 浏览器开始发送请求头时触发。
     * 作用:
     *  用于计算从发起请求到接收到响应的时间。
     *  可以结合 responseStart 和 responseEnd 分析服务器的响应性能。
     * */
    var requestStart: Long = 0L

    /**
     * 描述: 浏览器开始接收到服务器响应的时间点。
     * 触发时机: 浏览器接收到第一个字节的响应数据时触发。
     * 作用:
     *  用于计算服务器的响应时间。
     *  与 requestStart 的差值表示服务器处理请求的时间。
     * */
    var responseStart: Long = 0L

    /**
     * 描述: 浏览器接收到完整的服务器响应的时间点。
     * 触发时机: 浏览器接收到响应的最后一个字节时触发。
     * 作用:
     *  用于计算从发起请求到接收完整响应的时间。
     *  与 responseStart 的差值表示数据传输的时间。
     * */
    var responseEnd: Long = 0L

    /**
     * 描述: 浏览器开始解析 HTML 文档的时间点。
     * 触发时机: 浏览器开始构建 DOM 树时触发。
     * 作用:
     *  用于分析 HTML 文档的解析时间。
     *  与 responseEnd 的差值表示从接收到响应到开始解析 HTML 的时间。
     * */
    var domLoading: Long = 0L

    /**
     * 描述: HTML 文档解析完成，DOM 树构建完成的时间点。
     * 触发时机: 浏览器完成 DOM 树的构建时触发。
     * 作用:
     *  用于分析 HTML 文档的解析耗时。
     *  与 domLoading 的差值表示 DOM 树的构建时间。
     * */
    var domInteractive: Long = 0L

    /**
     * 描述: DOMContentLoaded 事件开始触发的时间点。
     * 触发时机: 浏览器完成 DOM 树的构建，并开始执行 DOMContentLoaded 事件监听器时触发。
     * 作用:
     *  用于分析页面的初始渲染时间。
     *  适合用来衡量页面的可交互性。
     * */
    var domContentLoadedEventStart: Long = 0L

    /**
     * 描述: DOMContentLoaded 事件结束的时间点。
     * 触发时机: 所有 DOMContentLoaded 事件监听器执行完成时触发。
     * 作用:
     *  与 domContentLoadedEventStart 的差值表示 DOMContentLoaded 事件的执行时间。
     * */
    var domContentLoadedEventEnd: Long = 0L

    /**
     * 描述: load 事件开始触发的时间点。
     * 触发时机: 浏览器完成页面的所有资源加载（包括图片、CSS、JS 等）后触发。
     * 作用:
     *  用于分析页面的完整加载时间。
     *  适合用来衡量页面的最终加载完成时间。
     * */
    var loadEventStart: Long = 0L

    /**
     * 描述: load 事件结束的时间点。
     * 触发时机: 所有 load 事件监听器执行完成时触发。
     * 作用:
     *  与 loadEventStart 的差值表示 load 事件的执行时间。
     *  与 navigationStart 的差值表示页面的总加载时间。
     * */
    var loadEventEnd: Long = 0L

}