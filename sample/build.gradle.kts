// A small app that uses the library's modules. It is not published.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures { buildConfig = true; compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext.junit)
    implementation(project(":core-creative-model"))
    implementation(project(":core-creative-renderer"))
    implementation(project(":core-creative-history-engine"))
    implementation(project(":core-creative-history-compose"))
    implementation(project(":core-creative-documents-engine"))
    implementation(project(":core-creative-documents-compose"))
    implementation(project(":core-creative-canvas-engine"))
    implementation(project(":core-creative-canvas-compose"))
    implementation(project(":core-creative-templates-engine"))
    implementation(project(":core-creative-templates-compose"))
    implementation(project(":core-creative-layout-engine"))
    implementation(project(":core-creative-layout-compose"))
    implementation(project(":core-creative-effects-engine"))
    implementation(project(":core-creative-effects-compose"))
    implementation(project(":core-creative-masks-engine"))
    implementation(project(":core-creative-masks-compose"))
    implementation(project(":core-creative-text-engine"))
    implementation(project(":core-creative-text-compose"))
    implementation(project(":core-creative-export-engine"))
    implementation(project(":core-creative-export-compose"))
    implementation(project(":core-creative-ai-tools-engine"))
    implementation(project(":core-creative-ai-tools-compose"))
    implementation(project(":core-creative-ai-images-engine"))
    implementation(project(":core-creative-ai-images-compose"))

    implementation(project(":core-logging"))
}
