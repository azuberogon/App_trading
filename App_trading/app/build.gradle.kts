import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleService) apply false
    id("org.jetbrains.dokka") version "1.9.10"
//    id("com.google.gms.google-services") version "4.4.2" apply false


}

android {
    namespace = "com.example.app_trading"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app_trading"
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
    kotlinOptions {
        jvmTarget = "11"
    }
}
tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(file("docs"))
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //firebase desde libs.versions.toml
    implementation(libs.firebaseAuth)
    implementation(libs.firebaseStore)
    implementation(libs.firebaseStorage)
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.google.code.gson:gson:2.8.9")


    //para que se haga la tabla y poder mostrar datos en graficos
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")
   // implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


    //implementation("com.google.firebase:firebase-auth:22.1.1")
//Firebase tutorial
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    //analisis de firebase
    implementation("com.google.firebase:firebase-analytics")
    //autenticacion de firebase
    //implementation("com.google.firebase:firebase-auth:23.2.0")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.3.0")


}