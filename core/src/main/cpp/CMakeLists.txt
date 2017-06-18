cmake_minimum_required(VERSION 3.5)

project(secure-keys)

link_directories(${JNI_LIBRARIES})

add_library(secure-keys

            SHARED

			secure-keys.cpp
			crypto/aes.cpp
			crypto/base64.cpp
			crypto/crypto_wrapper.cpp

			crypto/aes.h
			crypto/base64.h
			crypto/crypto_wrapper.h)

set(CMAKE_CXX_FLAGS "-fPIC")
set_target_properties(secure-keys PROPERTIES CMAKE_SHARED_LINKER_FLAGS "-fPIC")