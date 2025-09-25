./gradlew :app:assembleDebug

SCRIPT_DIR=$(dirname "$0")
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

adb -s emulator-5554 install -t "$SCRIPT_DIR/$APK_PATH"
adb -s emulator-5554 shell am start -n "com.proxy.service.funsdk/com.proxy.service.funsdk.MainActivity"