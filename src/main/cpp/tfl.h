#ifndef TFL_H
#define TFL_H

#include <jni.h>
#include <string>
#include <vector>

using namespace std;

// 全局上下文（单线程使用，根据 tranforcpp 确认）
static JNIEnv* g_env = nullptr;
static jobject g_player = nullptr;

inline void sendMessage(const string& msg) {
    jclass cls = g_env->FindClass("org/bukkit/entity/Player");
    jmethodID method = g_env->GetMethodID(cls, "sendMessage", "(Ljava/lang/String;)V");
    jstring jmsg = g_env->NewStringUTF(msg.c_str());
    g_env->CallVoidMethod(g_player, method, jmsg);
    g_env->DeleteLocalRef(jmsg);
}

inline string getName() {
    jclass cls = g_env->FindClass("org/bukkit/entity/Player");
    jmethodID method = g_env->GetMethodID(cls, "getName", "()Ljava/lang/String;");
    jstring jname = (jstring)g_env->CallObjectMethod(g_player, method);
    const char* cname = g_env->GetStringUTFChars(jname, nullptr);
    string result(cname);
    g_env->ReleaseStringUTFChars(jname, cname);
    return result;
}

inline void teleport(double x, double y, double z) {
    jclass locCls = g_env->FindClass("org/bukkit/Location");
    jclass worldCls = g_env->FindClass("org/bukkit/entity/Player");
    jmethodID getWorld = g_env->GetMethodID(worldCls, "getWorld", "()Lorg/bukkit/World;");
    jobject world = g_env->CallObjectMethod(g_player, getWorld);

    jmethodID locInit = g_env->GetMethodID(locCls, "<init>", "(Lorg/bukkit/World;DDD)V");
    jobject loc = g_env->NewObject(locCls, locInit, world, x, y, z);

    jmethodID tp = g_env->GetMethodID(worldCls, "teleport", "(Lorg/bukkit/Location;)Z");
    g_env->CallBooleanMethod(g_player, tp, loc);
}

typedef void (*CommandFunc)(vector<string>);

// JNI 执行入口（所有插件共享）
extern "C" JNIEXPORT void JNICALL Java_org_transformLanguage_Cpp_Command_1Bridge_Cpp_1tool_executeNative(
    JNIEnv *env, jclass cls, jlong funcPtr, jobject player, jobjectArray args) {
    g_env = env;
    g_player = player;

    CommandFunc func = (CommandFunc)funcPtr;
    jsize len = env->GetArrayLength(args);
    vector<string> vargs;

    for (jsize i = 0; i < len; i++) {
        jstring jstr = (jstring)env->GetObjectArrayElement(args, i);
        const char* cstr = env->GetStringUTFChars(jstr, nullptr);
        vargs.push_back(cstr);
        env->ReleaseStringUTFChars(jstr, cstr);
    }

    func(vargs);
}

// 注册命令宏（每个命令生成唯一的注册函数）
#define REGISTER_COMMAND(cmdname, func) \
extern "C" JNIEXPORT void JNICALL Java_org_transformLanguage_Cpp_Command_1Bridge_Cpp_1tool_register_##cmdname( \
    JNIEnv *env, jclass cls) { \
    jmethodID method = env->GetStaticMethodID(cls, "registerCommand", "(Ljava/lang/String;J)V"); \
    jstring name = env->NewStringUTF(#cmdname); \
    env->CallStaticVoidMethod(cls, method, name, (jlong)func); \
    env->DeleteLocalRef(name); \
}

#endif
