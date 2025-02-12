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
    //implementation("com.google.firebase:firebase-firestore:24.4.1") // Firestore אם תשתמשי
   // implementation("com.google.firebase:firebase-analytics:21.4.0") // Firebase Analytics (לא חובה)
    //implementation("com.google.firebase:firebase-analytics:21.3.0") // Firebase Analytics (לא חובה)

    // RecyclerView (הכרחי כדי להציג נתונים)
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Navigation Components
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}