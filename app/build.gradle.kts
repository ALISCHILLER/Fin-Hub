import java.io.File
import org.gradle.api.tasks.Exec
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.msa.finhub"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.msa.finhub"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            // اگر پوشش تست می‌خواهی و خطا گرفتی، این دو خط را حذف کن یا Jacoco اضافه کن
            // enableUnitTestCoverage = true
            // enableAndroidTestCoverage = true
            isMinifyEnabled = false
            buildConfigField("boolean", "ENABLE_NETWORK_LOGS", "true")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            buildConfigField("boolean", "ENABLE_NETWORK_LOGS", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }

    testOptions { unitTests.isIncludeAndroidResources = true }

    lint {
        abortOnError = true
        checkReleaseBuilds = false
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //di koin
    implementation(dependency.koin.androidx.compose)
    implementation(dependency.koin.test)
    testImplementation(dependency.koin.android.test)
    // Ktor Core + Plugins
    implementation(platform(dependency.ktor.bom))
    implementation(dependency.ktor.core)
    implementation(dependency.ktor.okhttp)
    implementation(dependency.ktor.auth)
    implementation(dependency.ktor.logging)
    implementation(dependency.ktor.negotiation)
    implementation(dependency.ktor.json)

    //coroutines
    implementation(dependency.coroutines.android)

    // log  timber
    implementation(dependency.timber.log)

    // image loader coil
    implementation(dependency.coil.image)

    // ---- Firebase (BOM)
    implementation(platform(dependency.firebaseBom))
    implementation(dependency.firebaseCrashlytics)
    implementation(dependency.firebaseAnalytics)

    // ---- Navigation
    implementation(libs.androidxNavigationCompose)

    // ---- Data & Security
    implementation(dependency.datastore)
    implementation(dependency.securitycrypto)
    implementation("androidx.compose.material:material-icons-extended")
    // Biometric and Fragment support
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    // Lifecycle utilities for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

}

