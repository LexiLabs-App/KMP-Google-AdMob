import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

/** REMEDIATION **/
dependencies {
    /** This patches all transitive dependencies that have vulnerabilities **/
    constraints {
        implementation(libs.remediate.okhttp) {
            version { strictly(libs.versions.remediate.okhttp.get()) }
            because("CVE-2021-0341")
        }
        implementation(libs.remediate.bitbucket) {
            version { strictly(libs.versions.remediate.bitbucket.get())}
            because("CVE-2024-29371")
        }
        implementation(libs.remediate.netty.codec.http){
            version { strictly(libs.versions.remediate.netty.get())}
            because("CVE-2025-67735")
        }
        implementation(libs.remediate.netty.codec.http2){
            version { strictly(libs.versions.remediate.netty.get())}
            because("CVE-2025-55163")
        }
        implementation(libs.remediate.google.protobuf.kotlin){
            version { strictly(libs.versions.remediate.google.protobuf.get())}
            because("CVE-2024-7254")
        }
        implementation(libs.remediate.google.protobuf.java){
            version { strictly(libs.versions.remediate.google.protobuf.get())}
            because("CVE-2024-7254")
        }
        implementation(libs.remediate.jdom){
            version { strictly(libs.versions.remediate.jdom.get())}
            because("CVE-2021-33813")
        }
        implementation(libs.remediate.apache.compress){
            version { strictly(libs.versions.remediate.apache.compress.get())}
            because("CVE-2024-26308")
        }
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Disregards expect/actual warnings
    // https://youtrack.jetbrains.com/issue/KT-61573
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosX64(),
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
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.google.ads)
            implementation(libs.google.ump)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.lexilabs.basic.ads)
            implementation(libs.lexilabs.basic.logging)
        }
    }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

