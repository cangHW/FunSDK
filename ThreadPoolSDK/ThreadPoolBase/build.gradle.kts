plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.proxy.service.threadpool.base"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    resourcePrefix = "cs_thread_base_"
}

dependencies {
    implementation(libs.android.appcompat)
    implementation(libs.android.kotlin)
    implementation(libs.cloud.api)

    api(libs.core.framework)

    runtimeOnly(project(":ThreadPoolSDK:ThreadPoolInfo"))

}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)