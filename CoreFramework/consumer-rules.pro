
########################   默认规则   ###########################
# 保留注解
-keepattributes *Annotation*

# 保留枚举类的 values() 和 valueOf() 方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留 Parcelable 的实现类
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留 Serializable 实现类的序列化 id 字段
-keep class * implements java.io.Serializable {
  private static final long serialVersionUID;
}

# 保留反射相关的类和方法
-keepclassmembers class * {
    public <init>(...);
    public static <fields>;
    public static <methods>;
}

# 保留自定义的 Application 类
-keep class * extends android.app.Application {
    public <init>(...);
}

# 忽略与 Android SDK 相关的警告
-dontwarn android.support.**
-dontwarn androidx.**
-dontwarn java.lang.invoke.**