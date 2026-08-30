package ru.asmelnikov.utils.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {
    @Serializable
    data object Competitions : Routes

    @Serializable
    data class Standings(val compId: String, val compUrl: String) : Routes

    @Serializable
    data class Team(val teamId: String) : Routes

    @Serializable
    data class Person(val personId: String) : Routes
}
