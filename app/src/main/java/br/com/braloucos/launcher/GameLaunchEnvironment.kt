package br.com.braloucos.launcher

/**
 * Describes the installed game's launch environment.
 *
 * This is metadata only: no game process is started and no libraries are loaded.
 */
data class GameLaunchEnvironment(
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val nativeLibraryDir: String,
    val supportedAbis: List<String>
) {
    companion object {
        fun from(
            installation: GeometryDashInstallation,
            supportedAbis: Array<String>
        ): GameLaunchEnvironment = GameLaunchEnvironment(
            packageName = installation.packageName,
            versionName = installation.versionName,
            versionCode = installation.versionCode,
            nativeLibraryDir = installation.nativeLibraryDir,
            supportedAbis = supportedAbis.toList()
        )
    }
}
