package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import java.util.UUID
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.ScorerEntity
import ru.asmelnikov.data.models.CompetitionScorersModelDTO
import ru.asmelnikov.data.models.PersonDTO
import ru.asmelnikov.data.models.ScorerDTO
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.Player
import ru.asmelnikov.domain.models.PlayerPosition
import ru.asmelnikov.domain.models.Scorer

fun CompetitionScorersModelDTO.toCompetitionScorersEntity(): CompetitionScorersEntity =
    CompetitionScorersEntity().apply {
        id = (
            this@toCompetitionScorersEntity.competition?.id ?: UUID.randomUUID()
                .hashCode()
            ).toString()
        scorers = realmListOf<ScorerEntity>().apply {
            this@toCompetitionScorersEntity.scorers?.map { it.toScorerEntity() }?.let { addAll(it) }
        }
        season = this@toCompetitionScorersEntity.season?.toCurrentSeasonEntity()
    }

fun CompetitionScorersEntity.toCompetitionScorers(): CompetitionScorers = CompetitionScorers(
    id = id,
    scorers = this.scorers?.map { it.toScorer() }
        ?.sortedWith(compareByDescending<Scorer> { it.goals }.thenByDescending { it.assists })
        ?: emptyList(),
    season = this.season.toCurrentSeason(),
)

fun ScorerDTO.toScorerEntity(): ScorerEntity = ScorerEntity().apply {
    assists = this@toScorerEntity.assists ?: 0
    goals = this@toScorerEntity.goals ?: 0
    penalties = this@toScorerEntity.penalties ?: 0
    playedMatches = this@toScorerEntity.playedMatches ?: 0
    player = this@toScorerEntity.player?.toPlayerEntity()
    team = this@toScorerEntity.team?.toTeamEmbeddedEntity()
}

fun PersonDTO.toPlayerEntity(): PlayerEntity = PlayerEntity().apply {
    id = this@toPlayerEntity.id ?: UUID.randomUUID().hashCode()
    dateOfBirth = this@toPlayerEntity.dateOfBirth ?: ""
    firstName = this@toPlayerEntity.firstName ?: ""
    lastName = this@toPlayerEntity.lastName ?: ""
    name = this@toPlayerEntity.name ?: ""
    nationality = this@toPlayerEntity.nationality ?: ""
    position = this@toPlayerEntity.position ?: ""
    shirtNumber = this@toPlayerEntity.shirtNumber
}

fun ScorerEntity.toScorer(): Scorer = Scorer(
    assists = assists,
    goals = goals,
    penalties = penalties,
    playedMatches = playedMatches,
    player = this.player.toPlayer(),
    team = this.team.toTeam(),
)

fun PlayerEntity?.toPlayer(): Player = Player(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    dateOfBirth = this?.dateOfBirth ?: "",
    firstName = this?.firstName ?: "",
    lastName = this?.lastName ?: "",
    name = this?.name ?: "",
    nationality = this?.nationality ?: "",
    position = PlayerPosition.fromValue(this?.position),
    shirtNumber = this?.shirtNumber ?: 0,
)
