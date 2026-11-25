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
        val compatibility = GeometryDashVersion.evaluate(game)
        val gameResult = when (compatibility) {
            GeometryDashCompatibility.NOT_INSTALLED ->
                "Geometry Dash: not installed or not visible"
            GeometryDashCompatibility.SUPPORTED ->
                "Geometry Dash: compatible\nVersion: ${game?.versionName} (${game?.versionCode})\nPackage: ${game?.packageName}\nNative libs: ${game?.nativeLibraryDir}"
            GeometryDashCompatibility.UNSUPPORTED_VERSION ->
                "Geometry Dash: unsupported version\nInstalled: ${game?.versionName}\nRequired: ${GeometryDashVersion.SUPPORTED_VERSION_NAME}"
        }

        val deviceAbis = Build.SUPPORTED_ABIS.joinToString()
        val nativeResult = runCatching {
            val bridge = NativeBridge()
            val version = bridge.nativeCoreVersion()
            val initialized = bridge.nativeInitialize()

            val environmentResult = if (
                compatibility == GeometryDashCompatibility.SUPPORTED && game != null
            ) {
                val environment = GameLaunchEnvironment.from(game, Build.SUPPORTED_ABIS)
                val configured = bridge.nativeConfigureGame(
                    packageName = environment.packageName,
                    versionName = environment.versionName,
                    nativeLibraryDir = environment.nativeLibraryDir,
                    deviceAbis = environment.supportedAbis.joinToString()
                )
                if (configured) {
                    "Game environment: metadata configured"
                } else {
                    "Game environment: configuration rejected"
                }
            } else {
                "Game environment: not configured (version gate)"
            }

            "Native core: $version\nInitialization: ${if (initialized) "ok" else "failed"}\n$environmentResult\nStatus: ${bridge.nativeGetStatus()}"
        }.getOrElse {
            "Native core error: ${it.message ?: "unknown error"}"
        }

        val message = TextView(this).apply {
            text = "Braloucos Launcher\n\n$gameResult\n\nDevice ABI: $deviceAbis\n\n$nativeResult"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
        }

        setContentView(message)
    }
}
