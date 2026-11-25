# Braloucos Android Launcher

Android launcher for the **Braloucos** Geometry Dash modding ecosystem.

The launcher provides the Android application layer, environment validation, runtime coordination, and the bridge to the native mod-loading core.

## Architecture

See [Architecture](docs/ARCHITECTURE.md) for the component boundaries, startup flow, JNI contract, storage model, package handling, and compatibility principles.

## Mod packages

Braloucos mod packages use the `.braloucos` extension. The launcher and runtime validate package metadata and compatibility before loading.

## Project boundaries

The Android launcher owns Android lifecycle and user interface concerns. The native core owns mod runtime initialization. The Braloucos SDK defines the interfaces used by mod authors.

## License

See [LICENSE](LICENSE).
