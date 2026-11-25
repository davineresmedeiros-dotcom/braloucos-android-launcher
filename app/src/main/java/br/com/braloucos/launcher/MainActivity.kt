package br.com.braloucos.launcher

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nativeResult = runCatching {
            val bridge = NativeBridge()
            val version = bridge.nativeCoreVersion()
            val initialized = bridge.nativeInitialize()
            val status = bridge.nativeGetStatus()

            "Native core: $version\nInitialization: ${if (initialized) status else "failed"}"
        }.getOrElse {
            "Native core error: ${it.message ?: "unknown error"}"
        }

        val message = TextView(this).apply {
            text = "Braloucos Launcher\n\n$nativeResult"
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }

        setContentView(message)
    }
}
