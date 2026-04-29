// Top-level build file where you can add configuration options common to all sub-projects/modules.

//Refer to: https://developers.google.com/maps/documentation/places/android-sdk/secrets-gradle-plugin#kotlin
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}

plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.apollographql.apollo") version "4.3.3" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}