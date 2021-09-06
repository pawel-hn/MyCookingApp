plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
    id ("androidx.navigation.safeargs.kotlin")
    id ("kotlin-parcelize")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion( "30.0.3")

    defaultConfig {
        applicationId = "pawel.hn.mycookingapp"
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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

    implementation(Dependencies.kotlinCore)
    implementation(Dependencies.kotlinKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)
    implementation(Dependencies.constraintLayout)
    implementation (Dependencies.annotation)
    implementation(Dependencies.liveData)
    implementation (Dependencies.fragmentKtx)
    implementation (Dependencies.viewModelKtx)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.junitTest)
    testImplementation(Dependencies.espressoCore)
    implementation(Dependencies.hiltCore)
    kapt(Dependencies.hiltKapt)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.paging)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGsonConverter)
    implementation(Dependencies.navigationFragmentKtx)
    implementation(Dependencies.navigationUiKtx)
    implementation(Dependencies.glideCore)
    kapt(Dependencies.glideKapt)
    implementation (Dependencies.firebaseAuth)
    implementation(Dependencies.fireStore)
    implementation(Dependencies.timber)
    implementation(Dependencies.dataStore)
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation(Dependencies.roomCore)
    kapt(Dependencies.roomKapt)
    implementation(Dependencies.roomKtx)


}
