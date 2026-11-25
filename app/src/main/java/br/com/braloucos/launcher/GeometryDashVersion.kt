package br.com.braloucos.launcher

/**
 * Compatibility gate for the currently targeted Geometry Dash Android build.
 * This does not load or patch the game.
 */
enum class GeometryDashCompatibility {
    NOT_INSTALLED,
    SUPPORTED,
    UNSUPPORTED_VERSION
}

object GeometryDashVersion {
    const val SUPPORTED_VERSION_NAME = "2.2091"

    fun evaluate(installation: GeometryDashInstallation?): GeometryDashCompatibility {
        if (installation == null) {
            return GeometryDashCompatibility.NOT_INSTALLED
        }

        return if (installation.versionName == SUPPORTED_VERSION_NAME) {
            GeometryDashCompatibility.SUPPORTED
        } else {
            GeometryDashCompatibility.UNSUPPORTED_VERSION
        }
    }
}
