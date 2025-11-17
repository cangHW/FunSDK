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

# ZSTD 包含目录（最小化版本）
include_directories(${CMAKE_SOURCE_DIR}/third_party/zstd/lib)
include_directories(${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common)
include_directories(${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress)
include_directories(${CMAKE_SOURCE_DIR}/third_party/zstd/lib/decompress)

unset(SOURCE_FILES)
unset(LZ4_SOURCE_FILES)
unset(CHACHA20_SOURCE_FILES)
unset(ZSTD_SOURCE_FILES)

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

# 添加 ZSTD 最小化源文件列表（只包含必要的）
set(ZSTD_SOURCE_FILES
    # Common - 必需的基础功能
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common/zstd_common.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common/error_private.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common/fse_decompress.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common/xxhash.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common/entropy_common.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/common/debug.c
    
    # Compress - 只包含单次压缩（不包含流式、字典等）
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_compress.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_compress_literals.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_compress_sequences.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_compress_superblock.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_fast.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_double_fast.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_lazy.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_opt.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/fse_compress.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/huf_compress.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/hist.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/compress/zstd_ldm.c
    
    # Decompress - 只包含单次解压
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/decompress/zstd_decompress.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/decompress/zstd_decompress_block.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/decompress/huf_decompress.c
    ${CMAKE_SOURCE_DIR}/third_party/zstd/lib/decompress/zstd_ddict.c
)

#message(STATUS "Source files: ${SOURCE_FILES}")
#message(STATUS "LZ4 Source files: ${LZ4_SOURCE_FILES}")
#message(STATUS "ZSTD Source files: ${ZSTD_SOURCE_FILES}")

# 添加可执行文件 - 包含项目源文件、LZ4、ChaCha20 和 ZSTD 源文件
add_library(${CMAKE_PROJECT_NAME} SHARED ${SOURCE_FILES} ${LZ4_SOURCE_FILES} ${CHACHA20_SOURCE_FILES} ${ZSTD_SOURCE_FILES})

# 编译优化选项（最小化体积）
if(CMAKE_BUILD_TYPE STREQUAL "Release")
    # 针对所有源文件的优化
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

# 编译选项：针对 LZ4 和 ZSTD 优化
target_compile_definitions(${CMAKE_PROJECT_NAME} PRIVATE
        LZ4_VERSION_MAJOR=1
        LZ4_VERSION_MINOR=9
        LZ4_VERSION_RELEASE=4
        
        # ZSTD 最小化配置
        ZSTD_DISABLE_ASM=1              # 禁用所有汇编优化，减小体积并避免链接错误
        # 注意：通过不包含相关源文件来实现最小化（不需要流式、字典、多线程等）
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