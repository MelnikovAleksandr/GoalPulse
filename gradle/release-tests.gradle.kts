import org.gradle.api.GradleException
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.io.File

fun failedTestNames(): List<String> {
    val xmls = fileTree(rootDir) {
        include("**/build/test-results/**/TEST-*.xml")
        include("**/build/outputs/androidTest-results/**/*.xml")
        exclude("**/binary/**")
    }
    val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
    return xmls.files.flatMap { file ->
        val doc = factory.newDocumentBuilder().parse(file)
        val nodes = doc.getElementsByTagName("testcase")
        (0 until nodes.length).mapNotNull { i ->
            val node = nodes.item(i) as org.w3c.dom.Element
            val failed = node.getElementsByTagName("failure").length > 0 ||
                node.getElementsByTagName("error").length > 0
            if (!failed) null
            else "${node.getAttribute("classname")}.${node.getAttribute("name")}"
        }
    }.distinct()
}

val java = File(System.getProperty("java.home"), "bin/java.exe")
val releaseTests = tasks.register<Exec>("releaseTests") {
    workingDir = rootDir
    environment("JAVA_HOME", System.getProperty("java.home"))
    standardOutput = System.out
    errorOutput = System.err
    isIgnoreExitValue = true
    commandLine(
        java.absolutePath, "-classpath", file("gradle/wrapper/gradle-wrapper.jar").absolutePath,
        "org.gradle.wrapper.GradleWrapperMain", "--no-daemon", "--console=plain",
        "testDebugUnitTest", "--rerun", "connectedDebugAndroidTest", "--rerun"
    )
    doLast {
        if (executionResult.get().exitValue == 0) return@doLast
        val failed = failedTestNames()
        failed.forEach { logger.lifecycle("FAILED TEST: $it") }
        throw GradleException(
            "Tests failed. APK was not built.\n" +
                failed.joinToString("\n") { "FAILED TEST: $it" }.ifEmpty { "FAILED TEST: see HTML report in build/reports/tests" }
        )
    }
}
gradle.projectsEvaluated {
    subprojects {
        tasks.withType<Test>().configureEach {
            testLogging.events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
            addTestListener(object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) = Unit
                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent != null) return
                    logger.lifecycle(
                        "UNIT TESTS ${project.name}: ${result.successfulTestCount} passed, " +
                            "${result.failedTestCount} failed, ${result.skippedTestCount} skipped"
                    )
                }
                override fun beforeTest(testDescriptor: TestDescriptor) = Unit
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                    val line = "${result.resultType} ${testDescriptor.className}.${testDescriptor.name}"
                    logger.lifecycle(if (result.resultType == TestResult.ResultType.FAILURE) "FAILED TEST: $line" else line)
                }
            })
        }
    }
    listOf("packageRelease", "packageReleaseBundle", "bundleRelease")
        .mapNotNull { project(":app").tasks.findByName(it) }
        .forEach { it.dependsOn(releaseTests) }
}
