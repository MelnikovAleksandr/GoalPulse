package ru.asmelnikov.domain.repository

import android.content.Intent
import ru.asmelnikov.domain.models.Match

interface MatchCalendarRepository {

    fun hasCalendarPermission(): Boolean

    suspend fun findScheduledMatchIds(matchIds: Collection<Int>): Set<Int>

    suspend fun findEventId(matchId: Int): Long?

    fun insertIntent(match: Match): Intent?

    fun viewIntent(match: Match): Intent?
}
