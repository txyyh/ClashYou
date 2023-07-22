## Clash You

**⚠ This page is translated by GPT 4.**

Based on [Clash For Android](https://github.com/Kr328/ClashForAndroid), a [clash](https://github.com/Dreamacro/clash) GUI designed for Android devices, using the Material You design language.

The latest version can be obtained from [Releases](https://github.com/Kr328/ClashForAndroid/releases).

### Version Features

- Adapted to new Android version permissions
- Application theme supports dynamic color picking
- UI following MD3 design style

### Implementation

Complete [clash](https://github.com/Dreamacro/clash) implementation ~~(no `external-controller`~~

### Runtime Requirements

- Android 5.0+ (minimum)
- Android 7.0+ (recommended)
- `armeabi-v7a`, `arm64-v8a`, `x86` or `x86_64` architecture

### License

See [LICENSE](./LICENSE) and [NOTICE](./NOTICE)

### Privacy Policy

See [Privacy Policy](./PRIVACY_POLICY.md)

### Building

1. Update submodules (in IDEA project `terminal`)
   ```sh
   git submodule update --init --recursive
   ```
2. Install **OpenJDK 11**, **Android SDK**, **CMake** and **Golang**
3. Create a new `local.properties` file in the project root directory and write the following content
   ```properties
   sdk.dir=/path/to/android-sdk
   ```
4. Create a new `signing.properties` file in the project root directory and write the following content
   ```properties
   keystore.path=/path/to/keystore/file
   keystore.password=<keystore password>
   key.alias=<key alias>
   key.password=<key password>
   ```
5. Build
   ```sh
   ./gradlew app:assembleFossRelease
   ```
6. Output file `app-<version>-foss-<arch>-release.apk` is located in the `app/build/outputs/apk/foss/release/` directory.
