// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10" apply false
}
val defaultVersionCode by extra(14)
val defaultVersionName by extra("1.2.0")
val releaseVersionSuffix by extra("-release")
val debugVersionSuffix by extra("-debug")
