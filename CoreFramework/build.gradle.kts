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

    resourcePrefix = "cs_core_fw_"
}

dependencies {
    implementation(libs.android.appcompat)
    implementation(libs.android.kotlin)
    api(libs.cloud.api)
    kapt(libs.cloud.compiler)

    implementation(libs.service.threadpool)
    implementation(libs.service.permission)

    // 协议库
    compileOnly(project(":ApiHttpSDK:ApiHttpBase"))
    compileOnly(project(":ImageLoaderSDK:ImageLoaderBase"))
    compileOnly(project(":WebViewSDK:WebViewBase"))
    compileOnly(project(":DocumentsSDK:Image:DocumentImageBase"))
    compileOnly(project(":DocumentsSDK:Pdf:DocumentPdfBase"))

    // 压缩库
    implementation(libs.file.zip)

    // json 解析库
    implementation(libs.json.gson)

    // 类似 SharedPreferences, 高性能键值对存储库
    implementation(libs.sp.mmkv)

    // work 基于条件任务管理
//    implementation(libs.android.work.runtime)

//    implementation("androidx.startup:startup-runtime:1.1.1")

}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)