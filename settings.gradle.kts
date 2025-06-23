pluginManagement {
    repositories {
        maven {
            name = "Repo"
            setUrl("file://${rootDir}/Repo")
        }
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
        maven {
            name = "Repo"
            setUrl("file://${rootDir}/Repo")
        }
        google()
        mavenCentral()
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
include(":WebViewSDK:WebViewDSBridge")

include(":PermissionSDK:PermissionBase")
include(":PermissionSDK:PermissionInfo")

include(":DocumentsSDK:Pdf:DocumentPdfBase")
include(":DocumentsSDK:Pdf:DocumentPdfInfo")

include(":DocumentsSDK:Image:DocumentImageBase")
include(":DocumentsSDK:Image:DocumentImageInfo")

include(":LogFileSDK:LogFileInfo")

include(":ApmSDK:ApmInfo")
