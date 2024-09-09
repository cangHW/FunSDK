plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.core"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        kapt {
            arguments {
                arg("CLOUD_MODULE_NAME", project.name)
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.cloud.api)
    kapt(libs.cloud.compiler)

    // 协议库
    api(project(":ThreadPoolSDK:ThreadPoolBase"))
    api(project(":ApiHttpSDK:ApiHttpBase"))
    api(project(":ImageLoaderSDK:ImageLoaderBase"))
    api(project(":WebViewSDK:WebViewBase"))

    // 日志库
    implementation(libs.log.timber)

    // 压缩库
    implementation(libs.file.zip)

    // json 解析库
    implementation(libs.json.gson)

    // 类似 SharedPreferences, 高性能键值对存储库
    implementation(libs.sp.mmkv)

}

apply(from = File(project.rootDir.absolutePath, "plugins/script/maven_center.gradle").absolutePath)