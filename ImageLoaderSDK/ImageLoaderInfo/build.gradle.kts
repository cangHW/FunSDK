plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.imageloader.info"
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
//    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.cloud.api)
    kapt(libs.cloud.compiler)

    implementation(project(":ImageLoaderSDK:ImageLoaderBase"))
    implementation(libs.core.framework){
        exclude(group = "io.github.cangHW", module = "Service-ImageLoaderBase")
    }

    //view
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //lottie
    implementation("com.airbnb.android:lottie:6.0.0")
}

apply(from = File(project.rootDir.absolutePath, "plugins/script/maven_center.gradle").absolutePath)