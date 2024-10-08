import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    //alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinSerialization)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.soho.sohoapp.live"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.soho.sohoapp.live"
        minSdk = 24
        targetSdk = 34
        versionCode = 11
        versionName = "1.0.1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("config") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    flavorDimensions += "appType"

    productFlavors {
        create("development") {
            dimension = "appType"

            resValue("string", "app_name", "SohoLive Dev")

            buildConfigField("String", "BASE_URL", "\"https://staging.sohoapp.com/api/soho_live/\"")
            buildConfigField("String", "BASE_URL_TS", "\"https://ts-cluster-staging.soho.com.au/\"")
            buildConfigField("String", "TS_API_KEY", "\"03kWHv7zTzjGIY16OlPumRUuLV4w0bOW\"")
        }
        create("production") {
            dimension = "appType"

            resValue("string", "app_name", "SohoLive")

            buildConfigField("String", "BASE_URL", "\"https://api.sohoapp.com/api/soho_live/\"")
            buildConfigField(
                "String",
                "BASE_URL_TS",
                "\"https://s05321l9dthzorjap.a1.typesense.net/\""
            )
            buildConfigField("String", "TS_API_KEY", "\"V9k4duXQNm1LIHBwPxYKygmPTi1Pt2K3\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            //signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.ui)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.accompanist.systemuicontroller)

    //preference
    implementation(libs.androidx.datastore)

    //RootEncoder
    implementation(libs.rootencoder)

    //DI
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("io.insert-koin:koin-android:3.4.3")
    implementation("io.insert-koin:koin-androidx-compose:3.4.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")

    //Kort (Network)
    implementation(libs.ktor.core)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.negotiation)

    //bottom nav
    implementation(libs.androidx.navigation.compose.v282)

    //constraintlayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //cashe images
    implementation("io.coil-kt:coil-compose:2.6.0")

    //Facebook
    implementation("com.facebook.android:facebook-android-sdk:14.1.1")

    //LinkedIn SDK
    implementation("com.github.Sumudu-Sahan:LinkedInManager:1.01.00")

    //Google
    implementation("com.google.android.gms:play-services-auth:20.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")

    //Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.30.0")
    implementation(libs.androidx.camera.view)
    implementation(libs.material)

    /*implementation(libs.androidx.appcompat)
    implementation(libs.material)*/

    implementation("com.google.accompanist:accompanist-swiperefresh:0.27.0")

    //pager
    implementation("com.google.accompanist:accompanist-pager:0.30.1")

    //mux player
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(kotlin("reflect"))
}