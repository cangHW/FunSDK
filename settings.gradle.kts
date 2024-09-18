pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            isAllowInsecureProtocol = true
            url = uri("https://repository.git.100tal.com/repository/maven2_release.0a12cff94e/")
            credentials {
                        username = "book_bfe_android_ta_book_admin"
                        password = "TD49aVsEnS"
                    }
        }
    }
}

rootProject.name = "FunSDK"
include(":app")

include(":CoreFramework")

include(":ApiHttpSDK:ApiHttpBase")
include(":ApiHttpSDK:ApiHttpInfo")

include(":ThreadPoolSDK:ThreadPoolBase")
include(":ThreadPoolSDK:ThreadPoolInfo")

include(":ImageLoaderSDK:ImageLoaderBase")
include(":ImageLoaderSDK:ImageLoaderInfo")

include(":WebViewSDK:WebViewBase")
include(":WebViewSDK:WebViewInfo")
include(":WebViewSDK:WebViewBridgeDS")
