package br.com.braloucos.launcher

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val game = GeometryDashDetector.detect(this)
        val gameResult = game?.let {
            "Geometry Dash: detected\nVersion: ${it.versionName} (${it.versionCode})\nPackage: ${it.packageName}\nNative libs: ${it.nativeLibraryDir}"
        } ?: "Geometry Dash: not installed or not visible"

        val nativeResult = runCatching {
            val bridge = NativeBridge()
            val version = bridge.nativeCoreVersion()
            val initialized = bridge.nativeInitialize()
            val status = bridge.nativeGetStatus()

            "Native core: $version\nInitialization: ${if (initialized) status else "failed"}"
        }.getOrElse {
            "Native core error: ${it.message ?: "unknown error"}"
        }

        val deviceAbi = Build.SUPPORTED_ABIS.joinToString()
        val message = TextView(this).apply {
            text = "Braloucos Launcher\n\n$gameResult\n\nDevice ABI: $deviceAbi\n\n$nativeResult"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
        }

        setContentView(message)
    }
}
