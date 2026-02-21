package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.ScorerEntity
import ru.asmelnikov.data.models.CompetitionScorersModelDTO
import ru.asmelnikov.data.models.PersonDTO
import ru.asmelnikov.data.models.ScorerDTO
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.Player
import ru.asmelnikov.domain.models.Scorer
import java.util.UUID

fun CompetitionScorersModelDTO.toCompetitionScorersEntity(): CompetitionScorersEntity {
    return CompetitionScorersEntity().apply {
        id = (this@toCompetitionScorersEntity.competition?.id ?: UUID.randomUUID()
            .hashCode()).toString()
        scorers = realmListOf<ScorerEntity>().apply {
            this@toCompetitionScorersEntity.scorers?.map { it.toScorerEntity() }?.let { addAll(it) }
        }
        season = this@toCompetitionScorersEntity.season?.toCurrentSeasonEntity()
    }
}

fun CompetitionScorersEntity.toCompetitionScorers(): CompetitionScorers {
    return CompetitionScorers(
        id = id,
        scorers = this.scorers?.map { it.toScorer() } ?: emptyList(),
        season = this.season.toSeason(),
    )
}

fun ScorerDTO.toScorerEntity(): ScorerEntity {
    return ScorerEntity().apply {
        assists = this@toScorerEntity.assists ?: -1
        goals = this@toScorerEntity.goals ?: -1
        penalties = this@toScorerEntity.penalties ?: -1
        playedMatches = this@toScorerEntity.playedMatches ?: -1
        player = this@toScorerEntity.player?.toPlayerEntity()
        team = this@toScorerEntity.team?.toTeamEmbeddedEntity()
    }
}

fun PersonDTO.toPlayerEntity(): PlayerEntity {
    return PlayerEntity().apply {
        id = this@toPlayerEntity.id ?: UUID.randomUUID().hashCode()
        dateOfBirth = this@toPlayerEntity.dateOfBirth ?: ""
        firstName = this@toPlayerEntity.firstName ?: ""
        lastName = this@toPlayerEntity.lastName ?: ""
        name = this@toPlayerEntity.name ?: ""
        nationality = this@toPlayerEntity.nationality ?: ""
        position = this@toPlayerEntity.position ?: ""
        shirtNumber = this@toPlayerEntity.shirtNumber
    }
}

fun ScorerEntity.toScorer(): Scorer {
    return Scorer(
        assists = assists,
        goals = goals,
        penalties = penalties,
        playedMatches = playedMatches,
        player = this.player.toPlayer(),
        team = this.team.toTeam(),
    )
}

fun PlayerEntity?.toPlayer(): Player {
    return Player(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        lastName = this?.lastName ?: "",
        name = this?.name ?: "",
        nationality = this?.nationality ?: "",
        position = this?.position ?: "",
        shirtNumber = this?.shirtNumber ?: 0
    )
}