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
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.cloud.api)

    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation(project(":CoreFramework"))
    implementation(project(":ApiHttpSDK:ApiHttpInfo"))
    implementation(project(":ThreadPoolSDK:ThreadPoolInfo"))
    implementation(project(":ImageLoaderSDK:ImageLoaderInfo"))
    implementation(project(":WebViewSDK:WebViewInfo"))
    implementation(project(":WebViewSDK:WebViewBridgeDS"))
}

apply(from = File(project.rootDir.absolutePath, "Plugins/gradle/common.gradle").absolutePath)


