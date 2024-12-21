buildscript {

    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.17.0")
    }
}

allprojects {

    extra.apply {
        // Koin for Android
        set("koinVersion", "3.5.3")
        // Retrofit
        set("retrofitVersion", "2.9.0")
        // Okhttp
        set("okhttpVersion", "4.12.0")
        // Moshi
        set("moshiVersion", "1.14.0")
        // Orbit
        set("orbitVersion", "6.1.0")
        // Coil
        set("coilVersion",  "2.5.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.jvm") version "2.0.21" apply false
    id("com.android.library") version "8.1.4" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}