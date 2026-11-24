// A small app that uses the library's modules. It is not published.
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "noel.libs.sample"
    compileSdk { version = release(37) }

    defaultConfig {
        applicationId = "noel.libs.sample"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core-logging"))
}
