import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.kotlin.plugin.compose)
}

kotlin {

    // Disregards expect/actual warnings
    // https://youtrack.jetbrains.com/issue/KT-61573
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.bundles.compose.android)
            implementation(libs.google.ads)
            implementation(libs.google.ump)
        }
        commonMain.dependencies {
            implementation(libs.bundles.compose.common)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.lexilabs.basic.ads)
            implementation(libs.lexilabs.basic.logging)
        }
    }

    android {
        namespace = "app.lexilabs.example.basic.ads"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withHostTest {  }
        androidResources {
            enable = true
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
        @Suppress("UnstableApiUsage")
        optimization {
            minify = true
        }
    }
}
