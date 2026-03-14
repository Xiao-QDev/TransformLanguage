@echo off
REM compile_plugin.bat - Windows 编译脚本

echo ============================================
echo   TransformLanguage C++ Plugin Compiler
echo   Windows Version
echo ============================================
echo.

REM 设置变量
set PLUGIN_NAME=my_plugin
set JAVA_HOME=%JAVA_HOME%

REM 检查 JAVA_HOME
if "%JAVA_HOME%"=="" (
    echo [ERROR] JAVA_HOME is not set!
    echo Please install JDK and set JAVA_HOME environment variable.
    echo Example: set JAVA_HOME=C:\Program Files\Java\jdk-21
    pause
    exit /b 1
)

echo [INFO] Using Java from: %JAVA_HOME%
echo.

REM 创建输出目录
if not exist "build\natives" mkdir build\natives

REM 编译为 DLL
echo [BUILDING] Compiling %PLUGIN_NAME%.dll...
g++ -shared -o build\natives\%PLUGIN_NAME%.dll ^
    -I"%JAVA_HOME%\include" ^
    -I"%JAVA_HOME%\include\win32" ^
    -I"src\main\cpp" ^
    src\main\cpp\%PLUGIN_NAME%.cpp

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] ✓ Build completed successfully!
    echo Output file: build\natives\%PLUGIN_NAME%.dll
    echo.
    echo ============================================
    echo   Next Steps:
    echo ============================================
    echo   1. Copy the .dll file to your server:
    echo      server_folder/plugins/TransformLanguage/plugins/%PLUGIN_NAME%.dll
    echo.
    echo   2. Restart your Minecraft server
    echo.
    echo   3. Test the command in-game:
    echo      /tfl test [arguments]
    echo ============================================
    echo.
) else (
    echo.
    echo [ERROR] ✗ Build failed!
    echo Please check:
    echo   - MinGW/GCC is installed and in PATH
    echo   - JAVA_HOME is correctly set
    echo   - All required files exist
    echo.
)

pause
