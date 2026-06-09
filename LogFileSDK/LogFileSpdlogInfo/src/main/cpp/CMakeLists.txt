# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("logfile")

# 包含目录配置
include_directories(${CMAKE_SOURCE_DIR}/spdlog/include)

# 查找源文件
file(GLOB_RECURSE SOURCE_FILES "src/*.cpp")

# 添加共享库
add_library(${CMAKE_PROJECT_NAME} SHARED ${SOURCE_FILES})

# 编译优化选项（最小化体积）
if(CMAKE_BUILD_TYPE STREQUAL "Release")
    target_compile_options(${CMAKE_PROJECT_NAME} PRIVATE
        -Os                    # 优化大小
        -ffunction-sections    # 函数分段
        -fdata-sections        # 数据分段
    )

    # 链接优化
    target_link_options(${CMAKE_PROJECT_NAME} PRIVATE
        -Wl,--gc-sections      # 移除未使用的段
        -Wl,--strip-all        # 移除所有符号
        -s                     # 等同于 --strip-all
    )
endif()

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(
        ${CMAKE_PROJECT_NAME}
        # List libraries link to the target library
        android
        log
)
