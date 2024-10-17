plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.proxy.service.apihttp.base"
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
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.cloud.api)
    compileOnly(libs.core.framework)

    api("com.squareup.okhttp3:okhttp:4.11.0")
    api("com.squareup.retrofit2:retrofit:2.9.0")
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)