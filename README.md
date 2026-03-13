# ⚡ TransformLanguage <TFL>
*The Pure-JNI Native Plugin Loader for Spigot*

> **中文简介**：这是一个基于 **JNI (Java Native Interface)** 技术的 Minecraft Spigot 扩展插件。它允许开发者将 C++、Rust 或 Go 编译的原生模块，直接作为插件在服务端运行。

---

## 🚀 核心特性

### 🔗 JNI 架构
- 通过 JNI 直接调用本地代码，实现 **零延迟** 的 Java 与 Native 交互。
- 直接操作 JVM 内存，性能损耗极低

### 🧩 原生模块化
- **C++**：利用极致性能优化核心算法。
- **Rust**：利用内存安全特性编写防崩溃模块。
- **Go**：利用 Goroutine 编写高并发网络模块。
- *只需将编译后的 `.dll` 或 `.so` 文件放入插件文件夹即可运行。*

### ⚙️ 高性能桥接
- 提供了 Java 与 Native 的双向数据桥。
- 允许 Native 代码直接获取 Minecraft 对象引用（通过 JNI Env）。
- 适用于需要毫秒级响应的高性能场景（如反作弊、物理引擎）。

---

## 🏗️ 技术架构

TFL 采用 **"JNI 动态库加载"** 模式：

1.  **宿主 (Java)**：`TransformLanguage.jar` 在 `onEnable` 时初始化 JNI 环境。
2.  **加载 (Load)**：使用 JNI `FindClass` 机制加载Native 库。
3.  **绑定 (Bind)**：通过 JNI 函数注册表，将 Spigot 的事件循环与 Native 函数绑定。
4.  **运行 (Run)**：Native 代码直接在 JVM 的同一进程内执行。

---

## 📚 开发者指南 (示例)

### 如何编写一个 TransformLanguage 模块?

#### 1. 编写 C++ 代码插件
```cpp
// native_module.cpp
extern "C" {
    // JNI 导出函数
    JNIEXPORT void JNICALL Java_com_transformlanguage_TFL_nativeTick
    (JNIEnv *env, jobject instance) {
        // 直接操作 Minecraft 逻辑
    }
}
