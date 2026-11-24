plugins {
    id("noel.libs.android.library")
    id("noel.libs.android.compose")
}
android { namespace = "noel.libs.creative.compose.common" }
dependencies {
    api(project(":core-creative-model"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
