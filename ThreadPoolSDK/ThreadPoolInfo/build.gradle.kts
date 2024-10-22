plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.threadpool.info"
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
//    implementation(libs.android.core.ktx)
    implementation(libs.android.appcompat)
    implementation(libs.cloud.api)
    kapt(libs.cloud.compiler)
    implementation(project(":ThreadPoolSDK:ThreadPoolBase"))
    implementation(libs.core.framework){
        exclude(group = "io.github.cangHW", module = "Service-ThreadPoolBase")
    }

    implementation(libs.bundles.thread)
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)
