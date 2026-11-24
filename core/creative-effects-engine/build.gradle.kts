plugins {
    id("noel.libs.android.library")
}
android { namespace = "noel.libs.creative.effects.engine" }
dependencies {
    api(project(":core-creative-model"))
    api(project(":core-creative-renderer"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
