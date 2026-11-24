plugins {
    id("noel.libs.android.library")
}
android { namespace = "noel.libs.creative.export.engine" }
dependencies {
    api(libs.androidx.media3.transformer)
    implementation(libs.androidx.media3.effect)
    implementation(libs.androidx.media3.exoplayer)
    api(project(":core-creative-model"))
    api(project(":core-creative-renderer"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
