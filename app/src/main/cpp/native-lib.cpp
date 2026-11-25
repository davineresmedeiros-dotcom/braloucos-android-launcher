#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_br_com_braloucos_launcher_MainActivity_nativeCoreVersion(
    JNIEnv* env,
    jobject /* thiz */
) {
    return env->NewStringUTF("0.1.0 (base)");
}
