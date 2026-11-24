plugins {
    id("noel.libs.android.library")
}
android { namespace = "noel.libs.creative.text.engine" }
dependencies {
    api(project(":core-creative-model"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
