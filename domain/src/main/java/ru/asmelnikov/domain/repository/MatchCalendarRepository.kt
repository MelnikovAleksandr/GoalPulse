package ru.asmelnikov.domain.repository

import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.utils.Resource

interface MatchCalendarRepository {

    fun hasCalendarPermission(): Boolean

    suspend fun findScheduledMatchIds(matchIds: Collection<Int>): Set<Int>

    suspend fun addMatch(match: Match): Resource<Unit>

    suspend fun removeMatch(matchId: Int): Resource<Unit>
}
