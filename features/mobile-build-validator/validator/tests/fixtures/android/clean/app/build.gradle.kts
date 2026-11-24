plugins { id("com.android.application") }

android {
    namespace = "com.example.good"
    defaultConfig {
        minSdk = 26
        buildConfigField("String", "API_BASE", "\"https://api.example.com\"")
    }
    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("UPLOAD_KEYSTORE") ?: "missing.jks")
            storePassword = System.getenv("UPLOAD_STORE_PASSWORD")
            keyAlias = "upload"
            keyPassword = System.getenv("UPLOAD_KEY_PASSWORD")
        }
    }
    flavorDimensions += "env"
    productFlavors {
        create("staging") {
            dimension = "env"
            applicationIdSuffix = ".stg"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
        }
        create("staging") {
            initWith(getByName("release"))
            applicationIdSuffix = ".staging"
        }
    }
}

dependencies {
    implementation("com.google.android.play:integrity:1.4.0")
}
