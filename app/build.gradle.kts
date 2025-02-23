plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.finalproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finalproject"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    // Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    // RecyclerView (הכרחי כדי להציג נתונים)
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // Navigation Components
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    //noinspection UseTomlInstead
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation (libs.material.v1100)
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.github.bumptech.glide:glide:4.12.0")
}