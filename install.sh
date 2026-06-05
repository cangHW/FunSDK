SCRIPT_DIR=$(dirname "$0")

./gradlew :app:assembleDebug
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

#./gradlew :app:assembleRelease
#APK_PATH="app/build/outputs/apk/release/app-release.apk"

adb install -t "$SCRIPT_DIR/$APK_PATH"
adb shell am start -n "com.proxy.service.funsdk/com.proxy.service.funsdk.MainActivity"