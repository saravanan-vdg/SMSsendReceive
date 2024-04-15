plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sai.mysms"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sai.mysms"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
// Room KTX
    implementation("androidx.room:room-ktx:2.6.1")


    // Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.42")
    // kapt "com.google.dagger:hilt-android-compiler:$hilt_version"
    //implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'
    kapt("com.google.dagger:hilt-compiler:2.42")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Hilt testing dependency
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.42")
    // Make Hilt generate code in the androidTest folder
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.42")
    kaptAndroidTest("androidx.hilt:hilt-compiler:1.0.0")

    implementation("com.airbnb.android:lottie:2.8.0")



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}