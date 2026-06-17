plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.debugbridge.nano"
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

    resourcePrefix = "cs_debugbridge_nano_"
}

dependencies {
    implementation(libs.android.appcompat)
    implementation(libs.android.kotlin)
    implementation(libs.cloud.api)
    implementation(libs.server.nanohttpd)
    implementation(libs.json.gson)

    compileOnly(project(":DebugBridgeSDK:DebugBridgeBase"))
    kapt(libs.cloud.compiler)
}

