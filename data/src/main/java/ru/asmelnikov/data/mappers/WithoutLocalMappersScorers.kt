package ru.asmelnikov.data.mappers

//import ru.asmelnikov.data.models.AreaDTO
//import ru.asmelnikov.data.models.CompetitionDTO
//import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
//import ru.asmelnikov.data.models.CurrentSeasonDTO
//import ru.asmelnikov.data.models.FiltersDTO
//import ru.asmelnikov.data.models.PlayerDTO
//import ru.asmelnikov.data.models.ScorerDTO
//import ru.asmelnikov.data.models.SeasonDTO
//import ru.asmelnikov.data.models.StandingDTO
//import ru.asmelnikov.data.models.TableDTO
//import ru.asmelnikov.data.models.TeamDTO
//import ru.asmelnikov.data.models.WinnerDTO
//import ru.asmelnikov.domain.models.Area
//import ru.asmelnikov.domain.models.Competition
//import ru.asmelnikov.domain.models.CompetitionStandings
//import ru.asmelnikov.domain.models.CurrentSeason
//import ru.asmelnikov.domain.models.Filters
//import ru.asmelnikov.domain.models.Player
//import ru.asmelnikov.domain.models.Scorer
//import ru.asmelnikov.domain.models.Season
//import ru.asmelnikov.domain.models.Standing
//import ru.asmelnikov.domain.models.Table
//import ru.asmelnikov.domain.models.Team
//import ru.asmelnikov.domain.models.Winner
//
//fun CompetitionStandingsModelDTO.toCompetitionStandings(): CompetitionStandings {
//    return CompetitionStandings(
//        id = (competition?.id ?: -1).toString(),
//        area = area.toArea(),
//        competition = this.competition.toCompetition(),
//        filters = filters.toFilters(),
//        season = season.toSeason(),
//        standings = standings?.map { it.toStanding() }?.filter { it.type == "TOTAL" }
//            ?: emptyList()
//    )
//}
//
//fun ScorerDTO.toScorer(): Scorer {
//    return Scorer(
//        assists = assists ?: -1,
//        goals = goals ?: -1,
//        penalties = penalties ?: -1,
//        playedMatches = playedMatches ?: -1,
//        player = this.player.toPlayer(),
//        team = this.team.toTeam(),
//    )
//}
//
////fun PlayerDTO?.toPlayer(): Player {
////    return Player(
////        dateOfBirth = this?.dateOfBirth ?: "",
////        firstName = this?.firstName ?: "",
////        id = this?.id ?: -1,
////        lastName = this?.lastName ?: "",
////        lastUpdated = this?.lastUpdated ?: "",
////        name = this?.name ?: "",
////        nationality = this?.nationality ?: "",
////        position = this?.position ?: "",
////        section = this?.section ?: "",
////        shirtNumber = this?.shirtNumber ?: -1
////    )
////}
//
//fun StandingDTO?.toStanding(): Standing {
//    return Standing(
//        group = this?.group ?: "",
//        stage = this?.stage ?: "",
//        type = this?.type ?: "",
//        table = this?.table?.map { it.toTable() } ?: emptyList()
//    )
//}
//
//fun TableDTO?.toTable(): Table {
//    return Table(
//        draw = this?.draw ?: -1,
//        form = this?.form ?: "",
//        goalDifference = this?.goalDifference ?: -1,
//        goalsAgainst = this?.goalsAgainst ?: -1,
//        goalsFor = this?.goalsFor ?: -1,
//        lost = this?.lost ?: -1,
//        playedGames = this?.playedGames ?: -1,
//        points = this?.points ?: -1,
//        position = this?.position ?: -1,
//        team = this?.team.toTeam(),
//        won = this?.won ?: -1
//    )
//}
//
////fun TeamDTO?.toTeam(): Team {
////    return Team(
////        crest = this?.crest ?: "",
////        id = this?.id ?: -1,
////        name = this?.name ?: "",
////        shortName = this?.shortName ?: "",
////        tla = this?.tla ?: ""
////    )
////}
//
//fun AreaDTO?.toArea(): Area {
//    return Area(
//        code = this?.code ?: "",
//        flag = this?.flag ?: "",
//        id = this?.id ?: -1,
//        name = this?.name ?: ""
//    )
//}
//
//fun CompetitionDTO?.toCompetition(): Competition {
//    return Competition(
//        area = this?.area.toArea(),
//        code = this?.code ?: "",
//        currentSeason = this?.currentSeason.toCurrentSeason(),
//        emblem = this?.emblem ?: "",
//        id = this?.id ?: -1,
//        lastUpdated = this?.lastUpdated ?: "",
//        name = this?.name ?: "",
//        numberOfAvailableSeasons = this?.numberOfAvailableSeasons ?: -1,
//        plan = this?.plan ?: "",
//        type = this?.type ?: "",
//        seasons = this?.seasons?.map { it.toSeason() }?.take(4) ?: emptyList()
//    )
//}
//
//fun FiltersDTO?.toFilters(): Filters {
//    return Filters(
//        season = this?.season ?: ""
//    )
//}
//
//fun CurrentSeasonDTO?.toCurrentSeason(): CurrentSeason {
//    return CurrentSeason(
//        currentMatchDay = this?.currentMatchDay ?: -1,
//        startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
//        endDate = this?.endDate ?: "",
//        id = this?.id ?: -1,
//        startDate = this?.startDate ?: "",
//        winner = this?.winner.toWinner()
//    )
//}
//
//fun WinnerDTO?.toWinner(): Winner {
//    return Winner(
//        address = this?.address ?: "",
//        clubColors = this?.clubColors ?: "",
//        crest = this?.crest ?: "",
//        founded = this?.founded ?: -1,
//        id = this?.id ?: -1,
//        lastUpdated = this?.lastUpdated ?: "",
//        name = this?.name ?: "",
//        shortName = this?.shortName ?: "",
//        tla = this?.tla ?: "",
//        website = this?.website ?: "",
//        venue = this?.venue ?: ""
//    )
//}