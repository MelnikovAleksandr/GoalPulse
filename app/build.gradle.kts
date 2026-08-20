import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {

    signingConfigs {
        create("release") {
            val p = Properties()
            p.load(project.rootProject.file("local.properties").reader())
            val file: String = p.getProperty("storeFile")
            val alias: String = p.getProperty("keyAlias")
            val storePas: String = p.getProperty("storePassword")
            val keyPas: String = p.getProperty("keyPassword")
            storeFile = file("\"$file\"")
            storePassword = "\"$storePas\""
            keyAlias = "\"$alias\""
            keyPassword = "\"$keyPas\""
        }
    }

    namespace = "ru.asmelnikov.goalpulse"
    compileSdk = 37

    defaultConfig {
        applicationId = "ru.asmelnikov.goalpulse"
        minSdk = 33
        targetSdk = 37
        versionCode = 5
        versionName = "5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        val variant = this
        outputs.all {
            val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val baseName = "GoalPulse"
            val buildType = variant.buildType.name
            val fileName = "${baseName}-${buildType}-" +
                    "v${defaultConfig.versionName}-" +
                    "vc${defaultConfig.versionCode}-" +
                    "${getDateTimeFormat()}.apk"

            outputImpl.outputFileName = fileName
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material3.window.size.class1)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.core.splashscreen)

    // Koin for Android
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // Module
    implementation(project(":feature:competitions_main"))
    implementation(project(":feature:competition_standings"))
    implementation(project(":feature:team_info"))
    implementation(project(":feature:person_info"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":utils"))
}

fun getDateTimeFormat(): String {
    val simpleDateFormat = SimpleDateFormat("ddMMyy")
    return simpleDateFormat.format(Date())
}