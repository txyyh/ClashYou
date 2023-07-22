plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
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
    compileOnly(project(":hideapi"))

    implementation(project(":core"))
    implementation(project(":service"))
    implementation(project(":design"))
    implementation(project(":common"))

    implementation(libs.kotlin.coroutine)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.coordinator)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.material)
    implementation(libs.androidx.splashscreen)
    implementation(libs.getactivity.xxpermission)
}

tasks.getByName("clean", type = Delete::class) {
    delete(file("release"))
}
android {
    defaultConfig {
        applicationId = "yos.clash.material"
        targetSdk = 34
    }
    compileSdk = 34
    buildToolsVersion = "34.0.0"
}
