cmake_minimum_required(VERSION 3.22.1)

project(braloucos_core LANGUAGES CXX)

add_library(
    braloucos_core
    SHARED
    native-lib.cpp
)

find_library(android_log_lib log)

target_compile_features(braloucos_core PRIVATE cxx_std_17)
target_compile_options(braloucos_core PRIVATE -Wall -Wextra)
target_link_libraries(braloucos_core PRIVATE ${android_log_lib})
