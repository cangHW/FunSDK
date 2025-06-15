# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("csPdf")

include_directories(${CMAKE_SOURCE_DIR}/include)

unset(SOURCE_FILES)

# 查找源文件
file(GLOB_RECURSE SOURCE_FILES "src/*.cpp")

message(STATUS "Source files: ${SOURCE_FILES}")

add_library(
        ${CMAKE_PROJECT_NAME}
        SHARED
        ${SOURCE_FILES}
)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.

# 添加预编译的pdfium库
add_library(aospPdfium SHARED IMPORTED)
set_target_properties(aospPdfium PROPERTIES
        IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/lib/${ANDROID_ABI}/libmodpdfium.so
)

# 添加预编译的c++_shared库
add_library(modc++_shared SHARED IMPORTED)
set_target_properties(modc++_shared PROPERTIES
        IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/lib/${ANDROID_ABI}/libc++_shared.so
)

# 添加预编译的ft2库
add_library(modft2 SHARED IMPORTED)
set_target_properties(modft2 PROPERTIES
        IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/lib/${ANDROID_ABI}/libmodft2.so
)

# 添加预编译的png库
add_library(modpng SHARED IMPORTED)
set_target_properties(modpng PROPERTIES
        IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/lib/${ANDROID_ABI}/libmodpng.so
)

target_link_libraries(
        ${CMAKE_PROJECT_NAME}
        aospPdfium
        modc++_shared
        modft2
        modpng
        jnigraphics
        # List libraries link to the target library
        android
        log)