plugins {
    id("noel.libs.android.library")
    id("noel.libs.android.compose")
}
android { namespace = "noel.libs.creative.effects.compose" }
dependencies {
    api(project(":core-creative-model"))
    api(project(":core-creative-effects-engine"))
    implementation(project(":core-creative-compose-common"))
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
