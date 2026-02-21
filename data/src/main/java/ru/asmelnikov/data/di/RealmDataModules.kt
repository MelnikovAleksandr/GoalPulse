package ru.asmelnikov.data.di

import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.PersonEntity
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.ContractEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.TimeEntity
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.RefereeEntity
import ru.asmelnikov.data.local.models.ScoreEntity
import ru.asmelnikov.data.local.models.ScorerEntity
import ru.asmelnikov.data.local.models.SquadByPositionEntity
import ru.asmelnikov.data.local.models.StandingEntity
import ru.asmelnikov.data.local.models.TableEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity

internal val entities = setOf(
    CompetitionScorersEntity::class,
    ScorerEntity::class,
    PlayerEntity::class,
    CompetitionStandingsEntity::class,
    CompetitionEmbeddedEntity::class,
    StandingEntity::class,
    TableEntity::class,
    CompetitionEntity::class,
    AreaEntity::class,
    CurrentSeasonEntity::class,
    MatchesEntity::class,
    MatchEntity::class,
    RefereeEntity::class,
    ScoreEntity::class,
    TimeEntity::class,
    TeamInfoEntity::class,
    PersonEntity::class,
    SquadByPositionEntity::class,
    ContractEntity::class,
    TeamEmbeddedEntity::class
)