import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.realm) apply false
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.ktlint)
}

apply(from = "gradle/release-tests.gradle.kts")

val ktlintVersion = "1.6.0"

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    extensions.configure<KtlintExtension> {
        version.set(ktlintVersion)
        ignoreFailures.set(false)
        outputToConsole.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.HTML)
        }
        filter {
            exclude { it.file.path.contains("${File.separator}build${File.separator}") }
        }
    }
}
