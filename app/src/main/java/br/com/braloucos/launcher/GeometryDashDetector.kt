package br.com.braloucos.launcher

import android.content.Context
import android.content.pm.PackageManager

 data class GeometryDashInstallation(
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val nativeLibraryDir: String
)

object GeometryDashDetector {
    const val PACKAGE_NAME = "com.robtopx.geometryjump"

    fun detect(context: Context): GeometryDashInstallation? {
        val packageInfo = try {
            context.packageManager.getPackageInfo(PACKAGE_NAME, 0)
        } catch (_: PackageManager.NameNotFoundException) {
            return null
        }

        val applicationInfo = packageInfo.applicationInfo
        return GeometryDashInstallation(
            packageName = PACKAGE_NAME,
            versionName = packageInfo.versionName ?: "unknown",
            versionCode = packageInfo.versionCode.toLong(),
            nativeLibraryDir = applicationInfo?.nativeLibraryDir.orEmpty()
        )
    }
}
