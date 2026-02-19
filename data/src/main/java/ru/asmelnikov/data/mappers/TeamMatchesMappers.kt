package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.TeamMatchesEntity
import ru.asmelnikov.data.models.CompetitionMatchesDTO
import ru.asmelnikov.data.models.MatchDTO
import ru.asmelnikov.domain.models.TeamMatches

fun CompetitionMatchesDTO.toTeamMatchesEntity(teamId: String): TeamMatchesEntity {
    return TeamMatchesEntity(
        id = teamId,
        season = createYearRange(
            this.matches?.firstOrNull()?.season?.startDate ?: "",
            this.matches?.firstOrNull()?.season?.endDate ?: ""
        ),
        matchesCompleted = groupMatchesByDateCompletedDTO(matches ?: emptyList()),
        matchesAhead = groupMatchesByDateAheadDTO(matches ?: emptyList())
    )
}

fun groupMatchesByDateCompletedDTO(matches: List<MatchDTO>): RealmList<MatchEntity> {
    return realmListOf<MatchEntity>().apply {
        addAll(matches.filter { it.status == "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
            .sortedByDescending { it.utcDate }
            .map { it.toMatchEntity() })
    }
}

fun groupMatchesByDateAheadDTO(matches: List<MatchDTO>): RealmList<MatchEntity> {
    return realmListOf<MatchEntity>().apply {
        addAll(matches.filter { it.status != "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
            .sortedBy { it.utcDate }
            .map { it.toMatchEntity() })
    }
}

fun TeamMatchesEntity.toTeamMatches(): TeamMatches {
    return TeamMatches(
        id = id,
        season = season,
        matchesCompleted = matchesCompleted?.map { it.toMatches() } ?: emptyList(),
        matchesAhead = matchesAhead?.map { it.toMatches() } ?: emptyList()
    )
}