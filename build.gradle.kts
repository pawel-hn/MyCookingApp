// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:4.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${DependenciesVersions.kotlinVersion}")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${DependenciesVersions.hiltVersion}")
        classpath ("com.google.gms:google-services:${DependenciesVersions.googleServices}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${DependenciesVersions.navigationComponentVersion}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

tasks.create<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}