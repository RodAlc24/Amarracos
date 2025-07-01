plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.rodalc.amarracos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rodalc.amarracos"
        minSdk = 28
        targetSdk = 35
        versionCode = rootProject.extra["defaultVersionCode"] as Int
        versionName = rootProject.extra["defaultVersionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            versionNameSuffix = rootProject.extra["releaseVersionSuffix"] as String
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = false
            resValue(
                "string", "versionCode",
                "v${rootProject.extra["defaultVersionCode"]} ${rootProject.extra["defaultVersionName"]}$versionNameSuffix"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = rootProject.extra["debugVersionSuffix"] as String
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            resValue(
                "string", "versionCode",
                "v${rootProject.extra["defaultVersionCode"]} ${rootProject.extra["defaultVersionName"]}$versionNameSuffix"
            )
        }
        getByName("debug") {
            versionNameSuffix = rootProject.extra["debugVersionSuffix"] as String
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2025.06.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.room:room-ktx:2.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.compose:compose-compiler:0.1.0-dev17")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.06.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.patrykandpatrick.vico:compose:2.1.3")
    implementation("com.patrykandpatrick.vico:compose-m3:2.1.3")
}