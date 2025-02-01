plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    id("app.cash.sqldelight") version "2.0.2"
}

android {
    namespace = "com.example.pawpal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pawpal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.circleimageview)
    implementation(libs.androidx.room.common)
    implementation(libs.engage.core)
    implementation(libs.common)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //ROOM Baza podataka
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    //SQLDelight baza podataka
    implementation("app.cash.sqldelight:android-driver:2.0.2") // SQLDelight Android Driver
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //PayPal placanje
    implementation ("com.paypal.android:paypal-web-payments:1.7.1")
    implementation ("com.paypal.android:card-payments:1.7.1")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    //grafovi
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.pawpal.appdatabase")
        }
    }
}