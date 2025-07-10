plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.cloud.service")
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

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.android.appcompat)

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
    implementation(libs.service.permission)
    implementation(libs.service.logfile)
    implementation(libs.service.apm)

    implementation(libs.service.document.image)
//    implementation(libs.service.document.pdf)
    implementation(project(":DocumentsSDK:Pdf:DocumentPdfBase"))
    implementation(project(":WidgetSDK:WidgetInfo"))


//    implementation("com.github.yasith99:APNG-Drawable:1.0.1")

}

apply(from = File(project.rootDir.absolutePath, "Plugins/gradle/common.gradle").absolutePath)


