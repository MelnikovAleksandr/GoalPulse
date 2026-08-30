package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Matches
import ru.asmelnikov.utils.Resource

interface CompetitionStandingsRepository {

    suspend fun getCompetitionStandingsFromRemoteToLocalById(
        compId: String,
    ): Resource<Boolean>

    suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings?>

    suspend fun getCompetitionTopScorersBySeason(
        compId: String,
    ): Resource<Boolean>

    suspend fun getScorersFlowFromLocal(compId: String): Flow<CompetitionScorers?>

    suspend fun getAllMatchesFromRemoteToLocal(
        compId: String,
    ): Resource<Boolean>

    suspend fun getAllMatchesFlowFromLocal(
        compId: String,
    ): Flow<Matches?>

    suspend fun getHead2headById(
        matchId: Int,
    ): Resource<Head2head>
}
