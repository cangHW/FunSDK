
#准备上传的类库 module
upArray=(
  # 核心基础库
#  ":CoreFramework"

  # 网络库
#  ":ApiHttpSDK:ApiHttpBase"
#  ":ApiHttpSDK:ApiHttpInfo"

  # 线程库
#  ":ThreadPoolSDK:ThreadPoolBase"
#  ":ThreadPoolSDK:ThreadPoolInfo"

  # 图片库
#  ":ImageLoaderSDK:ImageLoaderBase"
#  ":ImageLoaderSDK:ImageLoaderInfo"

  # web 容器库
#  ":WebViewSDK:WebViewBase"
#  ":WebViewSDK:WebViewInfo"
#  ":WebViewSDK:WebViewDSBridge"

    # 权限库
#  ":Permission:PermissionBase"
#  ":Permission:PermissionInfo"

    # 日志文件库
  ":LogFileSDK:LogFileInfo"
)

type_params="r"

for element in "${upArray[@]}"
do
  if [ "$1" = $type_params ]; then
    ./gradlew "$element:uploadRemote" < /dev/null
  else
    ./gradlew "$element:uploadLocal" < /dev/null
  fi
done

if [ "$1" = $type_params ]; then
    echo "SDK 发布远程结束"
else
    echo "SDK 发布本地结束"
fi
