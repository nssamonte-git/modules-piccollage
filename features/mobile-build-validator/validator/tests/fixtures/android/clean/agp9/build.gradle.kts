plugins { id("com.android.library") }

// AGP 9 optimization DSL: R8 is on for release, so AND-RE-001 must not fire.
android {
    namespace = "com.example.good.agp9"
    defaultConfig {
        minSdk = 26
    }
    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }
}
