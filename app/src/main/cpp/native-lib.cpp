#include <jni.h>

namespace {
bool g_initialized = false;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_br_com_braloucos_launcher_NativeBridge_nativeCoreVersion(
    JNIEnv* env,
    jobject /* thiz */
) {
    return env->NewStringUTF("0.1.0 (base)");
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_br_com_braloucos_launcher_NativeBridge_nativeInitialize(
    JNIEnv* /* env */,
    jobject /* thiz */
) {
    // Minimal core initialization placeholder for the JNI stage.
    g_initialized = true;
    return g_initialized ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_br_com_braloucos_launcher_NativeBridge_nativeGetStatus(
    JNIEnv* env,
    jobject /* thiz */
) {
    return env->NewStringUTF(g_initialized ? "initialized (base)" : "not initialized");
}
