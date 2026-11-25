package br.com.braloucos.launcher

/**
 * Kotlin entry point for the native Braloucos core.
 *
 * Keep JNI declarations here instead of inside an Android Activity.
 */
class NativeBridge {

    external fun nativeCoreVersion(): String

    external fun nativeInitialize(): Boolean

    external fun nativeGetStatus(): String

    companion object {
        init {
            System.loadLibrary("braloucos_core")
        }
    }
}
