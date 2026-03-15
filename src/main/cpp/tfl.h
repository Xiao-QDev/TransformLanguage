#ifndef TFL_H
#define TFL_H

#include <jni.h>
#include <string>
#include <vector>

using namespace std;

thread_local JNIEnv* g_env = nullptr;
thread_local jobject g_player = nullptr;

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

#define REGISTER_COMMAND(name, func) \
extern "C" JNIEXPORT void JNICALL \
Java_org_transformLanguage_Cpp_CommandBridge_Cpp_0005ftool_executeNative_##name( \
    JNIEnv *env, jclass cls, jlong funcPtr, jobject player, jobjectArray args) { \
    g_env = env; g_player = player; \
    jsize len = env->GetArrayLength(args); \
    vector<string> vargs; \
    for (jsize i = 0; i < len; i++) { \
        jstring jstr = (jstring)env->GetObjectArrayElement(args, i); \
        const char* cstr = env->GetStringUTFChars(jstr, nullptr); \
        vargs.push_back(cstr); \
        env->ReleaseStringUTFChars(jstr, cstr); \
    } \
    func(vargs); \
} \
\
static struct __CommandRegistrar_##name { \
    __CommandRegistrar_##name() { \
        if (g_env) { \
            jclass cls = g_env->FindClass("org/transformLanguage/Cpp/Command_0005fBridge/Cpp_0005ftool"); \
            if (cls) { \
                jmethodID registerMethod = g_env->GetStaticMethodID(cls, "registerCommand", "(Ljava/lang/String;J)V"); \
                if (registerMethod) { \
                    jstring cmdName = g_env->NewStringUTF(#name); \
                    jlong funcPtr = (jlong)(void*)&func; \
                    g_env->CallStaticVoidMethod(cls, registerMethod, cmdName, funcPtr); \
                } \
            } \
        } \
    } \
} __registrar_##name;

#endif
