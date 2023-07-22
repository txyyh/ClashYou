## Clash You

📕 [English Version](./README_en.md)

基于 [Clash for Android](https://github.com/Kr328/ClashForAndroid)
，为安卓设备设计的 [clash](https://github.com/Dreamacro/clash) GUI，使用 Material You 设计语言。

可在 [Releases](https://github.com/Kr328/ClashForAndroid/releases) 获取最新版本。

### 版本特性

- 适配新安卓版本权限
- 应用主题支持动态取色
- 遵循 MD3 设计风格的 UI

### 实现

完整 [clash](https://github.com/Dreamacro/clash) 实现 ~~(无 `external-controller（外部控制器）`~~

### 运行环境要求

- Android 5.0+ (最低)
- Android 7.0+ (推荐)
- `armeabi-v7a` , `arm64-v8a`, `x86` 或 `x86_64` 架构

### 许可证

参见 [LICENSE](./LICENSE) 与 [NOTICE](./NOTICE)

### 隐私协议

参见 [隐私协议](./PRIVACY_POLICY.md)

### 构建

1. 更新子模块（IDEA 项目内 `终端`）
   ```sh
   git submodule update --init --recursive
   ```
2. 安装 **OpenJDK 11**, **Android SDK**, **CMake** 和 **Golang**
3. 在项目根目录新建 `local.properties`，并写入以下内容
   ```properties
   sdk.dir=/path/to/android-sdk
   ```
4. 在项目根目录新建 `signing.properties`，并写入以下内容
   ```properties
   keystore.path=/path/to/keystore/file（签名密钥路径）
   keystore.password=<签名密钥密码>
   key.alias=<签名密钥别名>
   key.password=<签名密钥密码>
   ```
5. 构建
   ```sh
   ./gradlew app:assembleFossRelease
   ```
6. 输出文件 `app-<version>-foss-<arch>-release.apk` 在 `app/build/outputs/apk/foss/release/` 目录下
