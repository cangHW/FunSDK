# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

cmake_minimum_required(VERSION 3.22.1)

project("nativelib")

# ============== xlog 配置 ==============

# 设置 C++ 标准
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# 定义宏
add_definitions(-DANDROID)
add_definitions(-D__ANDROID__)
add_definitions(-DUSING_XLOG_MARS_STANDALONE)

# 头文件搜索路径
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/comm
    ${CMAKE_CURRENT_SOURCE_DIR}/comm/xlogger
    ${CMAKE_CURRENT_SOURCE_DIR}/xlog
    ${CMAKE_CURRENT_SOURCE_DIR}/xlog/src
    ${CMAKE_CURRENT_SOURCE_DIR}/xlog/crypt
    ${CMAKE_CURRENT_SOURCE_DIR}/third_party
    ${CMAKE_CURRENT_SOURCE_DIR}/third_party/boost
    ${CMAKE_CURRENT_SOURCE_DIR}/third_party/micro-ecc
    ${CMAKE_CURRENT_SOURCE_DIR}/third_party/zstd/lib
)

# ============== zstd 库 ==============
set(ZSTD_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/third_party/zstd/lib)
file(GLOB ZSTD_SOURCES
    ${ZSTD_SRC_DIR}/common/*.c
    ${ZSTD_SRC_DIR}/compress/*.c
    ${ZSTD_SRC_DIR}/decompress/*.c
)

add_library(zstd STATIC ${ZSTD_SOURCES})
target_include_directories(zstd PUBLIC 
    ${ZSTD_SRC_DIR}
    ${ZSTD_SRC_DIR}/common
)
target_compile_definitions(zstd PRIVATE ZSTD_MULTITHREAD=0)

# ============== boost filesystem ==============
set(BOOST_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/third_party/boost/libs)
set(BOOST_SRC
    ${BOOST_SRC_DIR}/filesystem/src/codecvt_error_category.cpp
    ${BOOST_SRC_DIR}/filesystem/src/operations.cpp
    ${BOOST_SRC_DIR}/filesystem/src/path.cpp
    ${BOOST_SRC_DIR}/filesystem/src/path_traits.cpp
    ${BOOST_SRC_DIR}/filesystem/src/portability.cpp
    ${BOOST_SRC_DIR}/filesystem/src/unique_path.cpp
    ${BOOST_SRC_DIR}/filesystem/src/utf8_codecvt_facet.cpp
    ${BOOST_SRC_DIR}/system/src/error_code.cpp
    ${BOOST_SRC_DIR}/iostreams/src/mapped_file.cpp
    ${BOOST_SRC_DIR}/throw_exception.cpp
)

add_library(boost_filesystem STATIC ${BOOST_SRC})
target_include_directories(boost_filesystem PUBLIC 
    ${CMAKE_CURRENT_SOURCE_DIR}/third_party/boost
)
target_compile_definitions(boost_filesystem PRIVATE BOOST_FILESYSTEM_NO_CXX20_ATOMIC_REF)

# ============== comm 模块 ==============
set(COMM_SRC
    # 基础工具
    comm/autobuffer.cc
    comm/ptrbuffer.cc
    comm/mmap_util.cc
    comm/strutil.cc
    comm/tickcount.cc
    comm/time_utils.c
    comm/assert/__assert.c
    
    # xlogger
    comm/xlogger/xlogger.cc
    comm/xlogger/xloggerbase.c
    comm/xlogger/xlogger_category.cc
    comm/xlogger/loginfo_extract.c
    comm/unix/xlogger_threadinfo.cc
    
    # JNI 工具
    comm/jni/util/comm_function.cc
    comm/jni/util/scope_jenv.cc
    comm/jni/util/scoped_jstring.cc
    comm/jni/util/var_cache.cc
)

# ============== xlog 模块 ==============
set(XLOG_SRC
    # 核心
    xlog/src/appender.cc
    xlog/src/xlogger_interface.cc
    xlog/src/log_base_buffer.cc
    xlog/src/log_zlib_buffer.cc
    xlog/src/log_zstd_buffer.cc
    xlog/src/formater.cc
    
    # 加密
    xlog/crypt/log_crypt.cc
    third_party/micro-ecc/uECC.c
    
    # JNI
    xlog/jni/Java2C_Xlog.cc
    xlog/jni/ConsoleLog.cc
)

# ============== 主库 ==============
add_library(${CMAKE_PROJECT_NAME} SHARED
        JNI_OnLoad.cpp
    ${COMM_SRC}
    ${XLOG_SRC}
)

# 链接库
target_link_libraries(${CMAKE_PROJECT_NAME}
    zstd
    boost_filesystem
    android
    log
    z  # zlib (系统库)
)

# 编译选项
target_compile_options(${CMAKE_PROJECT_NAME} PRIVATE
    -fvisibility=hidden
    -fdata-sections
    -ffunction-sections
)

# 链接选项
target_link_options(${CMAKE_PROJECT_NAME} PRIVATE
    -Wl,--gc-sections
)
