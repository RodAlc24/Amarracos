// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0" // this version matches your Kotlin version
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0" apply false
}

val defaultVersionCode by extra(21)
val defaultVersionName by extra("1.3.2")
val releaseVersionSuffix by extra("-release")
val debugVersionSuffix by extra("-debug")
