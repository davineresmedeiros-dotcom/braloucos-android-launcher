package br.com.braloucos.launcher

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {

    private external fun nativeCoreVersion(): String

    companion object {
        init {
            System.loadLibrary("braloucos_core")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val status = runCatching { nativeCoreVersion() }
            .getOrElse { "Native core load failed: ${it.message ?: "unknown error"}" }

        val message = TextView(this).apply {
            text = "Braloucos Launcher\n\nNative core: $status"
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }

        setContentView(message)
    }
}
