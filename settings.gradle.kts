pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "GoalPulse"
include(":app")
include(":data")
include(":domain")
include(":utils")
include(":competitions_main")
include(":competition_standings")
include(":team_info")
include(":person_info")
