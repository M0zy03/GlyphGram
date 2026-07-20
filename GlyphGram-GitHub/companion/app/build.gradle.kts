plugins {
    id("com.android.application")
}

android {
    namespace = "dev.glyphgram.companion"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.glyphgram.companion"
        minSdk = 26
        targetSdk = 36
        versionCode = 3
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(files("libs/glyph-matrix-sdk-2.0.aar"))
}
