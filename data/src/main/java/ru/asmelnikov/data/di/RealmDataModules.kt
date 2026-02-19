package ru.asmelnikov.data.di

import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.AwayTeamEntity
import ru.asmelnikov.data.local.models.CoachEntity
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.ContractEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.FiltersEntity
import ru.asmelnikov.data.local.models.FullTimeEntity
import ru.asmelnikov.data.local.models.HalfTimeEntity
import ru.asmelnikov.data.local.models.HomeTeamEntity
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.MatchesByTourEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.RefereeEntity
import ru.asmelnikov.data.local.models.ScoreEntity
import ru.asmelnikov.data.local.models.ScorerEntity
import ru.asmelnikov.data.local.models.SeasonEntity
import ru.asmelnikov.data.local.models.SquadByPositionEntity
import ru.asmelnikov.data.local.models.SquadEntity
import ru.asmelnikov.data.local.models.StandingEntity
import ru.asmelnikov.data.local.models.TableEntity
import ru.asmelnikov.data.local.models.TeamEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.local.models.TeamMatchesEntity
import ru.asmelnikov.data.local.models.WinnerEntity

internal val entities = setOf(
    CompetitionScorersEntity::class,
    ScorerEntity::class,
    PlayerEntity::class,
    CompetitionStandingsEntity::class,
    CompetitionEmbeddedEntity::class,
    FiltersEntity::class,
    SeasonEntity::class,
    StandingEntity::class,
    TableEntity::class,
    TeamEntity::class,
    CompetitionEntity::class,
    AreaEntity::class,
    CurrentSeasonEntity::class,
    WinnerEntity::class,
    CompetitionMatchesEntity::class,
    MatchEntity::class,
    AwayTeamEntity::class,
    HomeTeamEntity::class,
    RefereeEntity::class,
    ScoreEntity::class,
    FullTimeEntity::class,
    HalfTimeEntity::class,
    MatchesByTourEntity::class,
    TeamInfoEntity::class,
    CoachEntity::class,
    SquadByPositionEntity::class,
    SquadEntity::class,
    ContractEntity::class,
    TeamMatchesEntity::class
)