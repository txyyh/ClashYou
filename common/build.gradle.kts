plugins {
    kotlin("android")
    id("com.android.library")
}

dependencies {
    compileOnly(project(":hideapi"))

    implementation(libs.kotlin.coroutine)
    implementation(libs.androidx.core)
}
android {
    defaultConfig {
        targetSdk = 34
    }
    compileSdk = 34
    buildToolsVersion = "34.0.0"
}
