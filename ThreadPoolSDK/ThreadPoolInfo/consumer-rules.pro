
########################   RxJava + RxAndroid   ###########################
# 保留 RxJava 的核心类
-keep class io.reactivex.** { *; }

# 保留 RxAndroid 的核心类
-keep class io.reactivex.android.** { *; }

# 保留 RxJava 和 RxAndroid 的注解
-keepattributes *Annotation*

# 忽略与 RxJava 和 RxAndroid 相关的警告
-dontwarn io.reactivex.**
-dontwarn io.reactivex.android.**