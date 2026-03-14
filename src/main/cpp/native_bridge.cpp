#include <jni.h>
#include <string>
#include <algorithm>

extern "C" {

JNIEXPORT jboolean JNICALL Java_org_transformLanguage_NativeBridge_initNative
  (JNIEnv *env, jclass cls) {
    return JNI_TRUE;
}

JNIEXPORT jstring JNICALL Java_org_transformLanguage_NativeBridge_transformText
  (JNIEnv *env, jclass cls, jstring input, jstring mode) {

    const jchar* inputChars = env->GetStringChars(input, nullptr);
    const char* modeStr = env->GetStringUTFChars(mode, nullptr);
    jsize len = env->GetStringLength(input);

    std::u16string text(reinterpret_cast<const char16_t*>(inputChars), len);
    std::string modeString(modeStr);

    if (modeString == "upper") {
        for (auto& c : text) {
            if (c >= u'a' && c <= u'z') c -= 32;
        }
    } else if (modeString == "lower") {
        for (auto& c : text) {
            if (c >= u'A' && c <= u'Z') c += 32;
        }
    } else if (modeString == "reverse") {
        std::reverse(text.begin(), text.end());
    }

    env->ReleaseStringChars(input, inputChars);
    env->ReleaseStringUTFChars(mode, modeStr);

    return env->NewString(reinterpret_cast<const jchar*>(text.c_str()), text.length());
}

}
