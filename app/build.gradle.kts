plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    id("com.cloud.service")
}

android {
    namespace = "com.proxy.service.funsdk"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.proxy.service.funsdk"
//        applicationId = "com.proxy.service.funsdk2"

        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
//    implementation(libs.android.core.ktx)
    implementation(libs.android.appcompat)
    implementation(libs.cloud.api)

    implementation(libs.android.view.flexbox)
    implementation(libs.android.view.constraintlayout)

    implementation(project(":CoreFramework"))
    implementation(project(":ApiHttpSDK:ApiHttpInfo"))
    implementation(project(":ThreadPoolSDK:ThreadPoolInfo"))
    implementation(project(":ImageLoaderSDK:ImageLoaderInfo"))
    implementation(project(":WebViewSDK:WebViewInfo"))
    implementation(project(":WebViewSDK:WebViewBridgeDS"))
}

apply(from = File(project.rootDir.absolutePath, "Plugins/gradle/common.gradle").absolutePath)


