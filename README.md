#  TransformLanguage | 通译互转

> **简介**：这是一个基于 **JNI (Java Native Interface)** 技术的 Minecraft Paper 插件。通过 C++ 原生代码实现文本转换功能（大写/小写/反转）。

> **About**: A Minecraft Paper plugin powered by **JNI (Java Native Interface)** with C++ native code for text transformation (upper/lower/reverse).

---

## 功能 | Features

- `/tfl help` - 显示帮助
- `/tfl version` - 显示版本
- `/tfl transform <模式> <文本...>` - 文本转换
  - `upper` - 转大写
  - `lower` - 转小写
  - `reverse` - 反转文本
- 支持多字节字符（中文等）
- JNI 原生性能

**示例：**
```
/tfl transform upper hello world
结果: HELLO WORLD

/tfl transform lower HELLO 世界
结果: hello 世界

/tfl transform reverse abc
结果: cba
```

注意: reverse 对 emoji 等代理对字符可能显示异常。

---

## 如何构建 | Building

### 1. 编译 C++ 原生库

**方法 1: 使用 CMake（推荐）**

1. 安装 CMake: https://cmake.org/download/
2. 安装 C++ 编译器（MinGW-w64 或 Visual Studio）
3. 运行：
```bash
mkdir build/cpp
cd build/cpp
cmake ../.. -DCMAKE_BUILD_TYPE=Release
cmake --build . --config Release
```

**方法 2: 直接使用 g++**

Windows:
```cmd
mkdir src\main\resources\natives
g++ -shared -o src/main/resources/natives/transformlang.dll -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32" src/main/cpp/native_bridge.cpp
```

Linux/Mac:
```bash
mkdir -p src/main/resources/natives
g++ -shared -fPIC -o src/main/resources/natives/libtransformlang.so -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" src/main/cpp/native_bridge.cpp
```

### 2. 构建插件

**Linux/macOS**: `./gradlew build`
**Windows**: `gradlew.bat build`

注意：必须先编译 C++ 代码，否则构建会失败。

---

## 文件结构 | Project Structure

```
TransformLanguage/
├── src/main/java/org/transformLanguage/
│   ├── TransformLanguage.java      # 主插件类
│   ├── Commands.java                # 命令处理
│   └── NativeBridge.java            # JNI 接口
├── src/main/cpp/
│   └── native_bridge.cpp            # C++ 实现
├── CMakeLists.txt                   # CMake 构建配置
└── build.gradle                     # Gradle 构建配置
```

---

## 开发指南 | Developer Guide

### JNI 接口

**Java 端** (`NativeBridge.java`):
```java
public class NativeBridge {
    public native static boolean initNative();
    public native static String transformText(String input, String mode);
}
```

**C++ 端** (`native_bridge.cpp`):
```cpp
JNIEXPORT jstring JNICALL Java_org_transformLanguage_NativeBridge_transformText
  (JNIEnv *env, jclass cls, jstring input, jstring mode) {
    // 使用 UTF-16 处理多字节字符
    const jchar* inputChars = env->GetStringChars(input, nullptr);
    jsize len = env->GetStringLength(input);
    std::u16string text(reinterpret_cast<const char16_t*>(inputChars), len);

    // 转换逻辑
    // ...

    env->ReleaseStringChars(input, inputChars);
    return env->NewString(reinterpret_cast<const jchar*>(text.c_str()), text.length());
}
```

### 关键实现细节

1. **库加载**: 静态块从 jar 资源提取并加载原生库
2. **错误处理**: 使用 `catch (Throwable)` 捕获 `UnsatisfiedLinkError`
3. **UTF-16 处理**: 使用 `GetStringChars` 正确处理多字节字符
4. **构建检查**: 缺少原生库时构建失败

---

## 许可 | License

MIT
