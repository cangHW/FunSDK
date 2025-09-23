
#准备上传的类库 module
upArray=(
  # 核心基础库
  ":CoreFramework"

  # 线程库
  ":ThreadPoolSDK:ThreadPoolInfo"
  ":ThreadPoolSDK:ThreadPoolBase"

  # 图片库
  ":ImageLoaderSDK:ImageLoaderInfo"
  ":ImageLoaderSDK:ImageLoaderBase"

  # 网络库
#  ":ApiHttpSDK:ApiHttpInfo"
#  ":ApiHttpSDK:ApiHttpBase"

  # web 容器库
#  ":WebViewSDK:WebViewInfo"
#  ":WebViewSDK:WebViewBase"
#  ":WebViewSDK:WebViewDSBridge"

    # 权限库
#  ":Permission:PermissionInfo"
#  ":Permission:PermissionBase"

    # 日志文件库
#  ":LogFileSDK:LogFileInfo"

    # 性能监控库
#  ":ApmSDK:ApmInfo"

  # 文档库
#  ":DocumentsSDK:Image:DocumentImageInfo"
#  ":DocumentsSDK:Image:DocumentImageBase"
#  ":DocumentsSDK:Pdf:DocumentPdfInfo"
#  ":DocumentsSDK:Pdf:DocumentPdfBase"

    # 视图 UI 库
#  ":WidgetSDK:WidgetInfo"
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
