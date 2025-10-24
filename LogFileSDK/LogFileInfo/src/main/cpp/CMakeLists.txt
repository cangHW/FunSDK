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
include_directories(${CMAKE_SOURCE_DIR}/third_party/lz4/lib)
include_directories(${CMAKE_SOURCE_DIR}/third_party/chacha20/include)

unset(SOURCE_FILES)
unset(LZ4_SOURCE_FILES)
unset(CHACHA20_SOURCE_FILES)

# 查找源文件
file(GLOB_RECURSE SOURCE_FILES "src/*.cpp")

# 添加 LZ4 源文件 
set(LZ4_SOURCE_FILES
        ${CMAKE_SOURCE_DIR}/third_party/lz4/lib/lz4.c
        ${CMAKE_SOURCE_DIR}/third_party/lz4/lib/lz4hc.c
        ${CMAKE_SOURCE_DIR}/third_party/lz4/lib/lz4frame.c
        ${CMAKE_SOURCE_DIR}/third_party/lz4/lib/xxhash.c
)

# 添加 ChaCha20 源文件
set(CHACHA20_SOURCE_FILES
        ${CMAKE_SOURCE_DIR}/third_party/chacha20/src/chacha20.c
)

#message(STATUS "Source files: ${SOURCE_FILES}")
#message(STATUS "LZ4 Source files: ${LZ4_SOURCE_FILES}")

# 添加可执行文件 - 包含项目源文件、LZ4 和 ChaCha20 源文件
add_library(${CMAKE_PROJECT_NAME} SHARED ${SOURCE_FILES} ${LZ4_SOURCE_FILES} ${CHACHA20_SOURCE_FILES})

# 编译选项：针对 LZ4 优化
target_compile_definitions(${CMAKE_PROJECT_NAME} PRIVATE
        LZ4_VERSION_MAJOR=1
        LZ4_VERSION_MINOR=9
        LZ4_VERSION_RELEASE=4
)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(
        ${CMAKE_PROJECT_NAME}
        # List libraries link to the target library
        android
        log
)