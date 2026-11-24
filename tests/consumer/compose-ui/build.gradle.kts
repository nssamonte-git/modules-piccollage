plugins { alias(libs.plugins.android.library)
 alias(libs.plugins.kotlin.compose)
}
android {
 namespace="noel.libs.consumer.compose.ui"
 compileSdk { version = release(37) }
 defaultConfig { minSdk=26 }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_11; targetCompatibility=JavaVersion.VERSION_11 }
 buildFeatures { compose=true }
}
dependencies { implementation("noel.libs:core-creative-text-compose:0.2.0") }
