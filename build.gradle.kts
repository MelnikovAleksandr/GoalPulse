plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.realm) apply false
    alias(libs.plugins.kotlin.plugin.compose)
}

apply(from = "gradle/release-tests.gradle.kts")
