pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        mavenLocal()
    }
}

rootProject.name = "GoalPulse"
include(":app")
include(":data")
include(":domain")
include(":utils")
include(":feature:competitions_main")
include(":feature:competition_standings")
include(":feature:team_info")
include(":feature:person_info")
