plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.projektwinterschlaf"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projektwinterschlaf"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        //um die neue Version in Room anzuzeigen
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    //Binding
    buildFeatures {
        viewBinding = true
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.fragment:fragment-ktx:1.6.0")

    //Firestore
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    implementation("com.google.firebase:firebase-firestore:24.9.1")

    //Storage zur Speicherung von User und Collections
    implementation("com.google.firebase:firebase-storage:20.3.0")

    //Database zur Speicherung von Daten wie Bewertungen Likes usw.
    implementation("com.google.firebase:firebase-database:20.3.0")

    //Firebase Datenbank für den Login
    implementation("com.google.firebase:firebase-auth:22.2.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    // Coil
    implementation("io.coil-kt:coil:2.4.0")

    // Room Dependencies
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    //Progressbar Implementierung
    implementation("com.mikhaellopez:circularprogressbar:3.1.0")

    //Longitude und Latitude
    implementation("com.google.android.gms:play-services-location:19.0.1")

    //Von externen Links Bilder setzen
    implementation("com.github.bumptech.glide:glide:4.16.0")
}