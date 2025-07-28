
########################   Glide   ###########################
# 保留 Glide 的生成代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public class * extends com.bumptech.glide.module.LibraryGlideModule
-keep public class * extends com.bumptech.glide.module.Initializer

# 保留 Glide 的注解处理器生成的代码
-keep @com.bumptech.glide.annotation.GlideModule class *
-keep @com.bumptech.glide.annotation.GlideExtension class *
-keep @com.bumptech.glide.annotation.GlideType class *
-keep @com.bumptech.glide.annotation.GlideOption class *

# 保留所有使用 Glide 注解的类及其成员
-keepclasseswithmembers class * {
    @com.bumptech.glide.annotation.GlideModule <methods>;
    @com.bumptech.glide.annotation.GlideExtension <methods>;
    @com.bumptech.glide.annotation.GlideType <methods>;
    @com.bumptech.glide.annotation.GlideOption <methods>;
}

# 保留 Parcelable 的实现类（Glide 使用了 Parcelable）
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留特定的内部类和方法
-keepnames class com.bumptech.glide.** { *; }
-keepclassmembers class com.bumptech.glide.** {
    *;
}
-dontwarn com.bumptech.glide.**


########################   Glide-webp   ###########################
-keep public class com.bumptech.glide.integration.webp.WebpImage { *; }
-keep public class com.bumptech.glide.integration.webp.WebpFrame { *; }
-keep public class com.bumptech.glide.integration.webp.WebpBitmapFactory { *; }


########################   lottie   ###########################
# 保留 Lottie 的核心类
-keep class com.airbnb.lottie.** { *; }

# 保留 Lottie 的 JSON 解析相关类
-keepnames class com.airbnb.lottie.parser.** { *; }

# 保留 Lottie 的反射相关类
-keepclassmembers class com.airbnb.lottie.** {
    *;
}

# 忽略与 Lottie 相关的警告
-dontwarn com.airbnb.lottie.**


