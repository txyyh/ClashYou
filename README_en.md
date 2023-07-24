## Clash You

**⚠ This page is translated by GPT 4.**

Based on [Clash for Android](https://github.com/Kr328/ClashForAndroid),
a [Clash](https://github.com/Dreamacro/clash) GUI designed for Android devices, using the Material
You design language.

The latest Releases version can be obtained from
[Releases](https://github.com/Kr328/ClashForAndroid/releases)
and CI version can be obtained from
[Actions](https://github.com/Yos-X/ClashYou/actions) (login is required, thanks to [@Light_summer](https://github.com/lightsummer233)).

### Version Features

- Adapted to new Android version permissions
- Application theme supports dynamic color picking
- UI following MD3 design style

### Attention

Clash You is based on **the final version** of Clash for Android, which has entered **a long-term non update state**.

Therefore, the old kernel used by Clash You may **not support** some features of the new Clash core.

For **the newer features** of Clash, consider the Clash Multiplatform project being developed by the original author [@Kr238](https://github.com/Kr328).

Telegram Channel：[Clash Multiplatform](https://t.me/+uCUxZwHNjZxlYThl)

### Feature

Fully feature of [Clash](https://github.com/Dreamacro/clash)

### Runtime Requirements

- Android 5.0+ (minimum)
- Android 12.0+ (recommended)
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
