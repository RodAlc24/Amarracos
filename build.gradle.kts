// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.10.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" // this version matches your Kotlin version
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10" apply false
}

val defaultVersionCode by extra(17)
val defaultVersionName by extra("1.2.3")
val releaseVersionSuffix by extra("-release")
val debugVersionSuffix by extra("-debug")
