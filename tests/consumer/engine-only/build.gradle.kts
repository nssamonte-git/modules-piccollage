plugins { alias(libs.plugins.android.library)
}
android {
 namespace="noel.libs.consumer.engine.only"
 compileSdk { version = release(37) }
 defaultConfig { minSdk=26 }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_11; targetCompatibility=JavaVersion.VERSION_11 }
}
dependencies { implementation("noel.libs:core-creative-history-engine:0.2.1") }
