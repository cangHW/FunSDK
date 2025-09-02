
########################   WebView   ###########################
# 保留 WebView 的 JavaScript 接口类
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 保留 WebView 的所有 public 方法
-keep public class android.webkit.WebView {
    public *;
}

# 保留自定义 WebView 的所有 public 方法
-keep public class com.proxy.service.webview.info.view.WebViewImpl {
    public *;
}

# 忽略与 WebView 相关的警告
-dontwarn android.webkit.**


########################   bytecode   ###########################
# 保留 Byte Buddy 的核心类
-keep class net.bytebuddy.** { *; }

# 保留 Byte Buddy Android 的核心类
-keep class net.bytebuddy.android.** { *; }

# 保留 Byte Buddy 的动态代理相关类
-keep class net.bytebuddy.dynamic.** { *; }

# 保留 Byte Buddy 的注解
-keepattributes *Annotation*

# 忽略与 Byte Buddy 相关的警告
-dontwarn net.bytebuddy.**
-dontwarn net.bytebuddy.android.**


