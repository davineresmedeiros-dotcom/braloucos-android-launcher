#include <jni.h>

#include <string>

namespace {
bool g_initialized = false;
bool g_game_configured = false;
std::string g_game_package;
std::string g_game_version;
std::string g_game_native_library_dir;
std::string g_device_abis;

bool copyJavaString(JNIEnv* env, jstring value, std::string& output) {
    if (value == nullptr) {
        return false;
    }

    const char* chars = env->GetStringUTFChars(value, nullptr);
    if (chars == nullptr) {
        return false;
    }

    output.assign(chars);
    env->ReleaseStringUTFChars(value, chars);
    return true;
}
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
    // Initializes only the Braloucos core placeholder.
    g_initialized = true;
    return JNI_TRUE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_br_com_braloucos_launcher_NativeBridge_nativeConfigureGame(
    JNIEnv* env,
    jobject /* thiz */,
    jstring package_name,
    jstring version_name,
    jstring native_library_dir,
    jstring device_abis
) {
    std::string package_value;
    std::string version_value;
    std::string library_dir_value;
    std::string abis_value;

    if (!copyJavaString(env, package_name, package_value) ||
        !copyJavaString(env, version_name, version_value) ||
        !copyJavaString(env, native_library_dir, library_dir_value) ||
        !copyJavaString(env, device_abis, abis_value)) {
        g_game_configured = false;
        return JNI_FALSE;
    }

    if (package_value.empty() || version_value.empty() ||
        library_dir_value.empty() || abis_value.empty()) {
        g_game_configured = false;
        return JNI_FALSE;
    }

    g_game_package = package_value;
    g_game_version = version_value;
    g_game_native_library_dir = library_dir_value;
    g_device_abis = abis_value;
    g_game_configured = true;
    return JNI_TRUE;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_br_com_braloucos_launcher_NativeBridge_nativeGetStatus(
    JNIEnv* env,
    jobject /* thiz */
) {
    if (g_game_configured) {
        return env->NewStringUTF(
            g_initialized ? "initialized; game environment metadata configured"
                         : "game environment metadata configured; core not initialized"
        );
    }

    return env->NewStringUTF(g_initialized ? "initialized (base)" : "not initialized");
}
