plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.proxy.service.widget.info"
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

    viewBinding {
        enable = true
    }

    resourcePrefix = "cs_widget_"
}

dependencies {
    implementation(libs.android.appcompat)
    implementation(libs.android.kotlin)
    implementation(libs.android.view.recycler)

    implementation(libs.cloud.api)
    kapt(libs.cloud.compiler)

    implementation(libs.core.framework)
    implementation(libs.service.threadpool)
    implementation(libs.service.imageloader)
}

apply(from = File(project.rootDir.absolutePath, "Plugins/script/maven_center.gradle").absolutePath)