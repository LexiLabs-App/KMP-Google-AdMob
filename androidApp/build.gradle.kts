plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.plugin.compose)
}

android {
    namespace = "app.lexilabs.example.basic.ads"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "app.lexilabs.example.basic.ads"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.project.version.code.get().toInt()
        versionName = libs.versions.project.version.name.get()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            ndk {
                debugSymbolLevel = "FULL"
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Depend on your shared KMP module
    implementation(project(":composeApp"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
}