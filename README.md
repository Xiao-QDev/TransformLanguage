# ⚡ TransformLanguage | 语言转换
> **简介**：这是一个基于 **JNI (Java Native Interface)** 技术的 Minecraft Spigot 扩展插件。它允许开发者将 C++、Rust 或 Go 编译的原生模块，直接作为插件在服务端运行。(😘后续可能会加入更多编程语言 或许吧?)
---
> **About**: A Minecraft Spigot extension powered by **JNI (Java Native Interface)** that allows developers to load and run native modules — including C++, Rust, or Go — directly as server plugins.(emmm yeah~ We'll support more programming languages later...maybe?)
---

## 🏗️ 如何构建

TFL 使用 **Gradle** 进行构建和打包：

1.  **运行构建命令**：`./gradlew build` (Linux/Mac) 或 `gradlew.bat build` (Windows)

---

## 📚 开发指南 (示例)

### 如何编写一个 TransformLanguage 模块?

#### 1. 编写 TFL 代码插件
```
C++ 示例: 
  -
