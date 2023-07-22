plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.library")
}

dependencies {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://maven.kr328.app/releases")
    }
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":service"))

    implementation(libs.kotlin.coroutine)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.coordinator)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.viewpager)
    implementation(libs.google.material)
    implementation(libs.getactivity.xxpermission)
}
android {
    defaultConfig {
        targetSdk = 34
    }
    compileSdk = 34
    buildToolsVersion = "34.0.0"
}
