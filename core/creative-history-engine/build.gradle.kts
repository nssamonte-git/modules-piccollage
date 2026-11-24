plugins {
    id("noel.libs.android.library")
}
android { namespace = "noel.libs.creative.history.engine" }
dependencies {
    api(project(":core-creative-model"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
