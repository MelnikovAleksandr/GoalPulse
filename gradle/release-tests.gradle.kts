import org.gradle.api.GradleException
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.io.File

val javaHome = System.getProperty("java.home")
val javaExe = File(
    javaHome,
    if (System.getProperty("os.name").startsWith("Windows")) "bin/java.exe" else "bin/java"
)

val releaseTests = tasks.register<Exec>("releaseTests") {
    workingDir = rootDir
    environment("JAVA_HOME", javaHome)
    standardOutput = System.out
    errorOutput = System.err
    isIgnoreExitValue = true
    commandLine(
        javaExe.absolutePath,
        "-Dorg.gradle.appname=gradlew",
        "-classpath", file("gradle/wrapper/gradle-wrapper.jar").absolutePath,
        "org.gradle.wrapper.GradleWrapperMain",
        "--no-daemon",
        "--console=plain",
        "testDebugUnitTest", "--rerun",
        "connectedDebugAndroidTest", "--rerun"
    )
    doLast {
        if (executionResult.get().exitValue != 0) {
            throw GradleException("Tests failed. Search the log for FAILED TEST. APK was not built.")
        }
    }
}

gradle.projectsEvaluated {
    subprojects {
        tasks.withType<Test>().configureEach {
            testLogging {
                events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
            }
            addTestListener(object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) = Unit
                override fun afterSuite(suite: TestDescriptor, result: TestResult) = Unit
                override fun beforeTest(testDescriptor: TestDescriptor) = Unit
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                    if (result.resultType != TestResult.ResultType.FAILURE) return
                    logger.lifecycle("FAILED TEST: ${testDescriptor.className}.${testDescriptor.name}")
                    result.exceptions.forEach { logger.lifecycle(it.toString()) }
                }
            })
        }
    }

    val androidTests = subprojects.mapNotNull { project ->
        val connected = project.tasks.findByName("connectedDebugAndroidTest") ?: return@mapNotNull null
        Triple(
            project.tasks.findByName("packageDebugAndroidTest"),
            project.tasks.findByName("createDebugAndroidTestApkListingFileRedirect"),
            connected
        )
    }
    androidTests.zipWithNext { (_, _, previous), (pack, listing, connected) ->
        listOfNotNull(pack, listing, connected).forEach { it.mustRunAfter(previous) }
    }
    androidTests.forEach { (pack, listing, connected) ->
        listing?.mustRunAfter(pack)
        connected.mustRunAfter(listOfNotNull(pack, listing))
    }

    listOf("packageRelease", "packageReleaseBundle", "bundleRelease")
        .mapNotNull { project(":app").tasks.findByName(it) }
        .forEach { it.dependsOn(releaseTests) }
}
