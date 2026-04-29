import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.apollographql.apollo")
    id("com.google.devtools.ksp")
}

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties = Properties().apply {
    secretsPropertiesFile.inputStream().use(::load)
}

val signingPropertiesFile = rootProject.file("signing.properties")
val signingProperties = Properties()
if (signingPropertiesFile.exists()) {
    signingPropertiesFile.inputStream().use(signingProperties::load)
}

fun Properties.requireString(key: String): String =
    getProperty(key) ?: error("Missing required property '$key' in properties file.")

fun String.unquoteIfWrapped(): String {
    val trimmed = trim()
    return if (trimmed.length >= 2 && trimmed.first() == '"' && trimmed.last() == '"') {
        trimmed.substring(1, trimmed.length - 1)
    } else {
        trimmed
    }
}

fun asBuildConfigString(value: String): String {
    val normalized = value.unquoteIfWrapped()
    return "\"${normalized.replace("\\", "\\\\").replace("\"", "\\\"")}\""
}

fun asManifestPlaceholder(value: String): String =
    value.unquoteIfWrapped()

android {
    namespace = "com.cornellappdev.transit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cornellappdev.transit"
        minSdk = 26
        targetSdk = 36
        versionCode = 12
        versionName = "2.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "MAPS_KEY",
            asBuildConfigString(secretsProperties.requireString("MAPS_KEY")),
        )
        manifestPlaceholders["MAPS_KEY"] = asManifestPlaceholder(secretsProperties.requireString("MAPS_KEY"))
    }

    signingConfigs {
        create("release") {
            if (signingProperties.isNotEmpty()) {
                storeFile = rootProject.file(signingProperties["KEYSTORE_PATH"].toString())
                storePassword = signingProperties["KEYSTORE_PASSWORD"].toString()
                keyAlias = signingProperties["KEY_ALIAS"].toString()
                keyPassword = signingProperties["KEY_PASSWORD"].toString()
            }
        }
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BACKEND_URL",
                asBuildConfigString(secretsProperties.requireString("DEBUG_BACKEND_URL")),
            )
            manifestPlaceholders["BACKEND_URL"] = asManifestPlaceholder(secretsProperties.requireString("DEBUG_BACKEND_URL"))
            buildConfigField(
                "String",
                "EATERY_URL",
                asBuildConfigString(secretsProperties.requireString("DEBUG_EATERY_URL")),
            )
            buildConfigField(
                "String",
                "UPLIFT_URL",
                asBuildConfigString(secretsProperties.requireString("DEBUG_UPLIFT_URL")),
            )
            buildConfigField("boolean", "ECOSYSTEM_FLAG", "true")
        }
        create("ecosystem") {
            initWith(getByName("debug"))
            isDebuggable = true
            buildConfigField(
                "String",
                "BACKEND_URL",
                asBuildConfigString(secretsProperties.requireString("DEBUG_BACKEND_URL")),
            )
            manifestPlaceholders["BACKEND_URL"] = asManifestPlaceholder(secretsProperties.requireString("DEBUG_BACKEND_URL"))
            buildConfigField(
                "String",
                "EATERY_URL",
                asBuildConfigString(secretsProperties.requireString("DEBUG_EATERY_URL")),
            )
            buildConfigField(
                "String",
                "UPLIFT_URL",
                asBuildConfigString(secretsProperties.requireString("DEBUG_UPLIFT_URL")),
            )
            buildConfigField("boolean", "ECOSYSTEM_FLAG", "true")
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            optimization {
                baselineProfile {
                    ignoreFromAllExternalDependencies = true
                }
            }
            buildConfigField(
                "String",
                "BACKEND_URL",
                asBuildConfigString(secretsProperties.requireString("PROD_BACKEND_URL")),
            )
            manifestPlaceholders["BACKEND_URL"] = asManifestPlaceholder(secretsProperties.requireString("PROD_BACKEND_URL"))
            buildConfigField(
                "String",
                "EATERY_URL",
                asBuildConfigString(secretsProperties.requireString("PROD_EATERY_URL")),
            )
            buildConfigField(
                "String",
                "UPLIFT_URL",
                asBuildConfigString(secretsProperties.requireString("PROD_UPLIFT_URL")),
            )
            buildConfigField("boolean", "ECOSYSTEM_FLAG", "true")
            if (signingProperties.isNotEmpty()) {
                signingConfig = signingConfigs.getByName("release")
            }
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
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // Work around device/OS install failures with embedded baseline profiles.
            excludes += "assets/dexopt/baseline.prof"
            excludes += "assets/dexopt/baseline.profm"
        }
    }
}

dependencies {
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.11.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Android Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material:1.7.5")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.activity:activity-compose:1.9.3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    "ecosystemImplementation"("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Lifecycle & Navigation
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Networking
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // Images & Media
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
    implementation("com.valentinilk.shimmer:compose-shimmer:1.3.1")
    implementation("androidx.media3:media3-common:1.3.0")

    // Maps
    implementation("com.google.maps.android:maps-compose:4.0.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // Data Storage
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    // Apollo GraphQL
    implementation("com.apollographql.apollo:apollo-runtime:4.3.3")

    // Bottom Sheet
    implementation("io.morfly.compose:advanced-bottomsheet-material3:0.1.0")
}

apollo {
    service("service") {
        packageName.set("com.cornellappdev.transit")
    }
}
