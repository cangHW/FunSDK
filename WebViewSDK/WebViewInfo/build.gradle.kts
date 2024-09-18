plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.webview.info"
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

    implementation(project(":WebViewSDK:WebViewBase"))
    implementation(libs.core.framework){
        exclude(group = "io.github.cangHW", module = "Service-WebViewBase")
    }
    implementation(libs.service.threadpool)

    //view
    //noinspection UseTomlInstead
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //noinspection UseTomlInstead
    implementation("net.bytebuddy:byte-buddy:1.14.4")
    //noinspection UseTomlInstead
    implementation("net.bytebuddy:byte-buddy-android:1.14.4")
}

apply(from = File(project.rootDir.absolutePath, "plugins/script/maven_center.gradle").absolutePath)
