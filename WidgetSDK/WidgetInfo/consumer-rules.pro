
########################   一般 view 规则   ###########################
# 保留所有继承自 View 的类
-keep class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留所有自定义 View 的 public 方法
-keepclassmembers class * extends android.view.View {
    public *;
}

# 保留反射调用的 View 方法
-keepclassmembers class android.view.View {
    public void set*(...);
    public void get*(...);
    public void is*(...);
}

# 忽略与 View 相关的警告
-dontwarn android.view.**