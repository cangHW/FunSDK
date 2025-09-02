
########################   Room   ###########################
# 保留 Room 的注解
-keepattributes *Annotation*

# 保留 Room 的实体类
-keep class androidx.room.** { *; }

# 保留 Room 的 DAO 接口
-keep interface androidx.room.** { *; }

# 保留 Room 的生成代码
-keep class * extends androidx.room.RoomDatabase {
    *;
}

# 忽略与 Room 相关的警告
-dontwarn androidx.room.**

-keep class com.proxy.service.apihttp.info.download.db.entity.** { *; }
-keep interface com.proxy.service.apihttp.info.download.db.dao.** { *; }


########################   Okhttp + retrofit   ###########################
# 保留 Retrofit 的接口
-keep class retrofit2.** { *; }
-keep class retrofit2.**$* { *; }

# 保留 Retrofit 的注解
-keepattributes *Annotation*

# 保留 Retrofit 的动态代理接口
-keep interface retrofit2.** { *; }

# 保留 OkHttp 的核心类
-keep class okhttp3.** { *; }
-keep class okhttp3.**$* { *; }

# 忽略与 Retrofit 和 OkHttp 相关的警告
-dontwarn retrofit2.**
-dontwarn okhttp3.**

-keep class com.proxy.service.apihttp.info.request.okhttp.interceptor.** { *; }
-keep class com.proxy.service.apihttp.info.request.retrofit.converter.** { *; }