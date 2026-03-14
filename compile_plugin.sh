#!/bin/bash
# compile_plugin.sh - Linux/Mac 编译脚本

echo "============================================"
echo "  TransformLanguage C++ Plugin Compiler"
echo "  Linux/Mac Version"
echo "============================================"
echo ""

# 设置变量
PLUGIN_NAME="my_plugin"

# 检查 JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
    echo "[ERROR] JAVA_HOME is not set!"
    echo "Please install JDK and set JAVA_HOME environment variable."
    echo "Example: export JAVA_HOME=/usr/lib/jvm/java-21-openjdk"
    exit 1
fi

echo "[INFO] Using Java from: $JAVA_HOME"
echo ""

# 创建输出目录
mkdir -p build/natives

# 检测操作系统
OS=$(uname -s)
echo "[INFO] Detecting OS: $OS"

# 根据系统设置编译参数
if [[ "$OS" == "Darwin" ]]; then
    # Mac
    EXT="dylib"
    JAVA_INCLUDE="$JAVA_HOME/include"
    JAVA_INCLUDE_OS="$JAVA_HOME/include/darwin"
    echo "[BUILDING] Compiling for Mac..."
elif [[ "$OS" == "Linux" ]]; then
    # Linux
    EXT="so"
    JAVA_INCLUDE="$JAVA_HOME/include"
    JAVA_INCLUDE_OS="$JAVA_HOME/include/linux"
    echo "[BUILDING] Compiling for Linux..."
else
    echo "[ERROR] Unsupported operating system: $OS"
    echo "Only Linux and Mac are supported."
    exit 1
fi

# 编译
g++ -shared -fPIC -o "build/natives/lib${PLUGIN_NAME}.${EXT}" \
    -I"$JAVA_INCLUDE" \
    -I"$JAVA_INCLUDE_OS" \
    -I"src/main/cpp" \
    "src/main/cpp/${PLUGIN_NAME}.cpp"

if [ $? -eq 0 ]; then
    echo ""
    echo "[SUCCESS] ✓ Build completed successfully!"
    echo "Output file: build/natives/lib${PLUGIN_NAME}.${EXT}"
    echo ""
    echo "============================================"
    echo "  Next Steps:"
    echo "============================================"
    echo "  1. Copy the library file to your server:"
    echo "     server_folder/plugins/TransformLanguage/plugins/lib${PLUGIN_NAME}.${EXT}"
    echo ""
    echo "  2. Restart your Minecraft server"
    echo ""
    echo "  3. Test the command in-game:"
    echo "     /tfl test [arguments]"
    echo "============================================"
    echo ""
else
    echo ""
    echo "[ERROR] ✗ Build failed!"
    echo "Please check:"
    echo "  - GCC/g++ is installed"
    echo "  - JAVA_HOME is correctly set"
    echo "  - JDK development headers are installed"
    echo ""
    exit 1
fi
