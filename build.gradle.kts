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
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}