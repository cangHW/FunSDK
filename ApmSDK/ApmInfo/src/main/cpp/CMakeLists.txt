cmake_minimum_required(VERSION 3.22.1)

project("csApm")

unset(SOURCE_FILES)

file(GLOB_RECURSE SOURCE_FILES
        "src/*.cpp"
        "common/*.cpp"
        "native_crash/*.cpp"
)

message(STATUS "Source files: ${SOURCE_FILES}")

add_library(
        ${CMAKE_PROJECT_NAME}
        SHARED
        ${SOURCE_FILES}
)

target_compile_options(${CMAKE_PROJECT_NAME} PRIVATE
        -fvisibility=hidden
        -fno-omit-frame-pointer
)

target_link_libraries(
        ${CMAKE_PROJECT_NAME}
        android
        log
)