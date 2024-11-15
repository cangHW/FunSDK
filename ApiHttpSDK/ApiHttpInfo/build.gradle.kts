plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.apihttp.info"
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
    implementation(libs.cloud.api)
    implementation(libs.android.appcompat)
    kapt(libs.cloud.compiler)

    implementation(project(":ApiHttpSDK:ApiHttpBase"))

    implementation(libs.core.framework) {
        exclude(group = "io.github.cangHW", module = "Service-ApihttpBase")
    }
    implementation(libs.service.threadpool)

    implementation(libs.bundles.http)

    implementation(libs.room.api)
    kapt(libs.room.compiler)
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)