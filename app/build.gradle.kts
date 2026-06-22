plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.cloud.service")
    id("kotlin-kapt")
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

        kapt {
            arguments {
                arg("CLOUD_MODULE_NAME", project.name)
            }
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "asd"
            keyPassword = "123456"
            storeFile = file("../Plugins/keystore/app.keystore")
            storePassword = "123456"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    viewBinding {
        enable = true
    }

    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res-widget",
                "src/main/res-framework",
                "src/main/res-imageloader",
                "src/main/res-permission",
                "src/main/res-api",
                "src/main/res-document",
                "src/main/res-web",
                "src/main/res-media",
                "src/main/res-apm"
            )
        }
    }
}

dependencies {
    implementation(libs.android.appcompat)
    kapt(libs.cloud.compiler)

    implementation(libs.android.view.flexbox)
    implementation(libs.android.view.constraintlayout)
    implementation(libs.android.view.viewpager)
    implementation(libs.test.demo.view)

    implementation(libs.cloud.api)

    implementation(libs.core.framework)
    implementation(libs.service.apihttp)
    implementation(libs.service.threadpool)
    implementation(libs.service.imageloader)
    implementation(libs.service.webview)
    implementation(libs.service.webview.bridge)
    implementation(libs.service.webview.monitor)
    implementation(libs.service.permission)

//    implementation(project(":LogFileSDK:LogFileXlogInfo"))
    implementation(libs.service.logfile)
    implementation(libs.service.apm)

    implementation(libs.service.document.image)
    implementation(libs.service.document.pdf)
    implementation(libs.service.widget)
    implementation(libs.service.media.camera)
    implementation(libs.service.webserver)
    debugImplementation(project(":ApiHttpSDK:ApiHttpWebServerPlugin"))

    //    implementation("com.github.yasith99:APNG-Drawable:1.0.1")
}

apply(from = File(project.rootDir.absolutePath, "Plugins/gradle/common.gradle").absolutePath)


