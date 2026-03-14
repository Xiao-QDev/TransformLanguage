#!/bin/bash
# 编译 C++ 插件示例

mkdir -p build/cpp_plugins

if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    # Windows
    g++ -shared -o build/cpp_plugins/my_plugin.dll examples/my_plugin.cpp -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/win32"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    g++ -shared -o build/cpp_plugins/libmy_plugin.dylib examples/my_plugin.cpp -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin"
else
    # Linux
    g++ -shared -fPIC -o build/cpp_plugins/libmy_plugin.so examples/my_plugin.cpp -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux"
fi

echo "C++ plugin compiled to build/cpp_plugins/"
