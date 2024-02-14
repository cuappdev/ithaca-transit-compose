// Top-level build file where you can add configuration options common to all sub-projects/modules.

//Refer to: https://developers.google.com/maps/documentation/places/android-sdk/secrets-gradle-plugin#kotlin
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}