plugins {
    id("noel.libs.android.library")
    id("noel.libs.android.compose")
}
android { namespace = "noel.libs.creative.canvas.compose" }
dependencies {
    api(project(":core-creative-model"))
    api(project(":core-creative-canvas-engine"))
    implementation(project(":core-creative-compose-common"))
    api(project(":core-creative-renderer"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
