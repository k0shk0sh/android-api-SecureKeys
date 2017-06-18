cmake_minimum_required(VERSION 3.5)

project(secure-keys-root)

if(ANDROID)
  message (STATUS "Android OS Detected")
else()
  message (STATUS "Not android.")
  find_package(JNI REQUIRED)
endif(ANDROID)

include_directories(${JNI_INCLUDE_DIRS})

include(CheckCXXCompilerFlag)
CHECK_CXX_COMPILER_FLAG("-std=c++11" COMPILER_SUPPORTS_CXX11)

add_subdirectory(core/src/main/cpp)