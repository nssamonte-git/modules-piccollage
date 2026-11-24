plugins {
    id("noel.libs.android.library")
}
android { namespace = "noel.libs.creative.model" }
dependencies {
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
