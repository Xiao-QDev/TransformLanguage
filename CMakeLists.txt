cmake_minimum_required(VERSION 3.10)
project(TransformLanguage)

set(CMAKE_CXX_STANDARD 11)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

add_library(transformlang SHARED src/main/cpp/native_bridge.cpp)
target_link_libraries(transformlang ${JNI_LIBRARIES})

set_target_properties(transformlang PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY "${CMAKE_SOURCE_DIR}/src/main/resources/natives"
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_SOURCE_DIR}/src/main/resources/natives"
)
