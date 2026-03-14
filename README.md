#  TransformLanguage | 通译互转

> **简介**：这是一个基于 **JNI (Java Native Interface)** 技术的 Minecraft Paper 插件。提供 C++ SDK，让开发者可以用纯 C++ 编写插件，无需处理 JNI 细节。

> **About**: A Minecraft Paper plugin powered by **JNI (Java Native Interface)** with C++ SDK for writing plugins in pure C++.

---

## 功能 | Features

- `/tfl transform <模式> <文本...>` - 文本转换（内置示例）
  - `upper` - 转大写
  - `lower` - 转小写
  - `reverse` - 反转文本
- `/tfl test [args...]` - C++ 插件示例
- C++ SDK - 用纯 C++ 编写插件

---

## C++ 插件开发 | C++ Plugin Development

### 快速开始

1. **创建插件文件** `my_plugin.cpp`:

```cpp
#include "tfl.h"

void myCommand(vector<string> args) {
    sendMessage("Hello from C++!");
    sendMessage("Your name: " + getName());

    if (args.size() > 0) {
        sendMessage("Arg: " + args[0]);
    }
}

REGISTER_COMMAND(mycommand, myCommand);
```

2. **编译插件**:

Windows:
```cmd
compile_plugin.bat
```

Linux/Mac:
```bash
chmod +x compile_plugin.sh
./compile_plugin.sh
```

3. **加载插件**: 将编译好的 `.dll`/`.so` 放到服务器 `java.library.path`

### C++ SDK API

**Player 操作:**
- `sendMessage(string msg)` - 发送消息
- `string getName()` - 获取玩家名
- `teleport(double x, double y, double z)` - 传送玩家

**注册命令:**
- `REGISTER_COMMAND(name, function)` - 注册命令处理函数

---

## 如何构建 | Building

### 1. 编译原生库（可选）

如果需要 transform 功能，先编译 C++ 代码：

**Windows:**
```cmd
mkdir src\main\resources\natives
g++ -shared -o src/main/resources/natives/transformlang.dll -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32" src/main/cpp/native_bridge.cpp
```

**Linux/Mac:**
```bash
mkdir -p src/main/resources/natives
g++ -shared -fPIC -o src/main/resources/natives/libtransformlang.so -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" src/main/cpp/native_bridge.cpp
```

### 2. 构建插件

**Linux/macOS**: `./gradlew build`
**Windows**: `gradlew.bat build`

---

## 许可 | License

MIT
