package br.com.braloucos.launcher

/**
 * Kotlin entry point for the native Braloucos core.
 *
 * JNI declarations live here instead of inside an Android Activity.
 */
class NativeBridge {

    external fun nativeCoreVersion(): String

    external fun nativeInitialize(): Boolean

    external fun nativeConfigureGame(
        packageName: String,
        versionName: String,
        nativeLibraryDir: String,
        deviceAbis: String
    ): Boolean

    external fun nativeGetStatus(): String

    companion object {
        init {
            System.loadLibrary("braloucos_core")
        }
    }
}
