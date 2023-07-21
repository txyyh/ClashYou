## Clash You

A Graphical user interface of [clash](https://github.com/Dreamacro/clash) for Android, uses Material You design specifications.

[Releases](https://github.com/Kr328/ClashForAndroid/releases)

**The following content comes from the original repository.**

### Feature

Fully feature of [clash](https://github.com/Dreamacro/clash) ~~(Exclude `external-controller`~~

### Requirement

- Android 5.0+ (minimum)
- Android 7.0+ (recommend)
- `armeabi-v7a` , `arm64-v8a`, `x86` or `x86_64` Architecture

### License

See also [LICENSE](./LICENSE) and [NOTICE](./NOTICE)

### Privacy Policy

See also [PRIVACY_POLICY.md](./PRIVACY_POLICY.md)

### Build

1. Update submodules

   ```bash
   git submodule update --init --recursive
   ```

2. Install **OpenJDK 11**, **Android SDK**, **CMake** and **Golang**

3. Create `local.properties` in project root with

   ```properties
   sdk.dir=/path/to/android-sdk
   ```

4. Create `signing.properties` in project root with

   ```properties
   keystore.path=/path/to/keystore/file
   keystore.password=<key store password>
   key.alias=<key alias>
   key.password=<key password>
   ```

5. Build

   ```bash
   ./gradlew app:assembleFossRelease
   ```

6. Pick `app-<version>-foss-<arch>-release.apk` in `app/build/outputs/apk/foss/release/`
