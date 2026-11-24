plugins {
    id("noel.libs.android.library")
}
android { namespace = "noel.libs.creative.ai.tools.engine" }
dependencies {
    testImplementation(project(":core-creative-history-engine"))
    api(project(":core-creative-model"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
