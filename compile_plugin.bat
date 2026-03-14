@echo off
REM 编译 C++ 插件示例 (Windows)

if not exist build\cpp_plugins mkdir build\cpp_plugins

g++ -shared -o build/cpp_plugins/my_plugin.dll examples/my_plugin.cpp -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32"

echo C++ plugin compiled to build/cpp_plugins/
