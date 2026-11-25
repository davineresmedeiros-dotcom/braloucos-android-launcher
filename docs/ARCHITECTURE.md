# Braloucos Android Launcher Architecture

## Purpose

The Braloucos Android Launcher is the Android-facing application for the Braloucos modding ecosystem. It is responsible for presenting the user interface, selecting the target game installation, preparing the Android runtime context, and coordinating startup of the native mod-loading core.

This document describes the intended architecture. It does not claim that the components are already implemented.

## Component boundaries

### Android application layer

Implemented in Kotlin and Android resources.

Responsibilities:
- Application entry point and lifecycle.
- Launcher screens, settings, and user-facing errors.
- Discovering and validating the target game package.
- Obtaining Android context, files directories, and permission state.
- Managing user-selected mod packages and configuration.
- Calling the native bridge only after required inputs are validated.

The UI must not contain native patching logic or mod-specific implementation.

### Runtime coordinator

A Kotlin component that owns the ordered startup process and reports state to the UI.

Suggested states:
- `Idle`
- `CheckingEnvironment`
- `PreparingPaths`
- `LoadingNativeCore`
- `InitializingMods`
- `Ready`
- `Failed`

Transitions should be explicit. A failure must stop later startup operations and provide a useful diagnostic; the launcher must not display success if native initialization failed.

### JNI bridge

A narrow Kotlin-to-C++ boundary. Kotlin passes validated paths and startup options; native code returns status codes and diagnostic messages.

The bridge should:
- Keep JNI signatures small and stable.
- Convert Java strings safely and release JNI resources.
- Avoid passing Android UI objects into native code.
- Catch native failures at the boundary where possible.
- Version the bridge contract when incompatible changes are introduced.

### Native loading core

The native core is responsible for platform-level initialization required by the modloader. It owns native library loading coordination, runtime path configuration, initialization checks, and the interface used by the mod system.

The launcher coordinates this core; it should not duplicate core logic in Kotlin.

### Game integration layer

Game-version-specific integration is isolated from general launcher code. Each supported game build should have explicit compatibility metadata and a dedicated integration implementation or configuration.

Do not assume offsets, symbols, or binary layouts are interchangeable between game versions. Unsupported versions should fail closed with a clear message.

## Startup sequence

1. Android creates the launcher activity and application context.
2. The launcher identifies the installed game and checks compatibility.
3. The runtime coordinator prepares private data and mod directories.
4. The native core is loaded and receives the required data paths.
5. The core initializes the mod runtime and discovers enabled packages.
6. The launcher starts or hands off to the game only after initialization reports success.

The sequence must be deterministic and must not depend on UI timing.

## Mod package handling

The launcher treats `.braloucos` as the Braloucos package extension. The extension is a file association, not a security guarantee.

Before installation or loading, a package should be validated for:
- A supported package format version.
- A well-formed manifest.
- Valid identifiers and declared entry points.
- Path traversal and duplicate-entry hazards.
- Compatibility declarations.
- Required permissions or capabilities.

Package contents should be extracted only into controlled directories. Native code in a mod is executable code and must not be treated as safe merely because it has the expected extension.

## Configuration and storage

Use app-private storage for runtime state, configuration, logs, and installed mod data. Keep user-imported package files separate from extracted runtime files.

Configuration should be versioned and migrated deliberately. Never silently overwrite a user's mod configuration when the launcher updates.

## Diagnostics

Use structured logs with component, severity, and startup phase. Diagnostics should include the launcher version, native-core version, Android ABI, and detected game version where available.

Do not include secrets or unnecessary personal data in logs. Error reporting should preserve the original failure reason.

## Dependency direction

- Android UI depends on the runtime coordinator.
- Runtime coordinator depends on the JNI bridge.
- JNI bridge depends on the native core contract.
- Native core depends on the SDK/runtime interfaces.
- Game-specific integration depends on the native core's integration interface.

The SDK must not depend on launcher UI classes.

## Compatibility principles

- Keep Android UI changes independent from native ABI changes.
- Keep game-specific code isolated from general runtime code.
- Version package manifests and the native bridge.
- Prefer explicit incompatibility errors over attempting to load an unknown game build.
