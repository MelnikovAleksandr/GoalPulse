package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.TimeEntity
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.RefereeEntity
import ru.asmelnikov.data.local.models.ScoreEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.data.models.MatchesDTO
import ru.asmelnikov.data.models.TimeDTO
import ru.asmelnikov.data.models.MatchDTO
import ru.asmelnikov.data.models.RefereeDTO
import ru.asmelnikov.data.models.ScoreDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.domain.models.Group
import ru.asmelnikov.domain.models.MatchTeam
import ru.asmelnikov.domain.models.Matches
import ru.asmelnikov.domain.models.Time
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchStatus
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Referee
import ru.asmelnikov.domain.models.Score
import ru.asmelnikov.domain.models.Stage
import ru.asmelnikov.domain.models.TeamMatches
import ru.asmelnikov.domain.models.TournamentType
import ru.asmelnikov.domain.models.Winner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID


fun MatchesDTO.toMatchesEntity(teamId: String? = null): MatchesEntity {
    return MatchesEntity().apply {
        id = teamId ?: competition?.id?.toString() ?: UUID.randomUUID().toString()
        seasonType = this@toMatchesEntity.matches?.firstOrNull()?.competition?.type ?: ""
        matches = realmListOf<MatchEntity>().apply {
            this@toMatchesEntity.matches?.map { it.toMatchEntity() }?.let { addAll(it) }
        }
    }
}

fun MatchDTO.toMatchEntity(): MatchEntity {
    return MatchEntity().apply {
        id = this@toMatchEntity.id ?: UUID.randomUUID().hashCode()
        area = this@toMatchEntity.area.toAreaEntity()
        competition = this@toMatchEntity.competition?.toCompetitionEmbeddedEntity()
        awayTeam = this@toMatchEntity.awayTeam?.toTeamEmbeddedEntity()
        group = this@toMatchEntity.group ?: ""
        homeTeam = this@toMatchEntity.homeTeam?.toTeamEmbeddedEntity()
        matchDay = this@toMatchEntity.matchDay ?: -1
        referees = realmListOf<RefereeEntity>().apply {
            this@toMatchEntity.referees?.map { it.toRefereeEntity() }?.let { addAll(it) }
        }
        score = this@toMatchEntity.score?.toScoreEntity()
        stage = this@toMatchEntity.stage ?: ""
        status = this@toMatchEntity.status ?: ""
        utcDate = this@toMatchEntity.utcDate ?: ""
    }
}

fun TeamInfoDTO.toTeamEmbeddedEntity(): TeamEmbeddedEntity {
    return TeamEmbeddedEntity().apply {
        id = this@toTeamEmbeddedEntity.id ?: UUID.randomUUID().hashCode()
        crest = this@toTeamEmbeddedEntity.crest ?: ""
        name = this@toTeamEmbeddedEntity.name ?: ""
        shortName = this@toTeamEmbeddedEntity.shortName ?: ""
        tla = this@toTeamEmbeddedEntity.tla ?: ""
        address = this@toTeamEmbeddedEntity.address ?: ""
        clubColors = this@toTeamEmbeddedEntity.clubColors ?: ""
        founded = this@toTeamEmbeddedEntity.founded ?: -1
        website = this@toTeamEmbeddedEntity.website ?: ""
        venue = this@toTeamEmbeddedEntity.venue ?: ""
    }
}

fun RefereeDTO.toRefereeEntity(): RefereeEntity {
    return RefereeEntity().apply {
        id = this@toRefereeEntity.id ?: UUID.randomUUID().hashCode()
        name = this@toRefereeEntity.name ?: ""
        nationality = this@toRefereeEntity.nationality ?: ""
        type = this@toRefereeEntity.type ?: ""
    }
}

fun ScoreDTO.toScoreEntity(): ScoreEntity {
    return ScoreEntity().apply {
        duration = this@toScoreEntity.duration ?: ""
        winner = this@toScoreEntity.winner ?: ""
        fullTime = this@toScoreEntity.fullTime?.toFullTimeEntity()
        halfTime = this@toScoreEntity.halfTime?.toFullTimeEntity()
    }
}

fun TimeDTO.toFullTimeEntity(): TimeEntity {
    return TimeEntity().apply {
        away = this@toFullTimeEntity.away ?: 0
        home = this@toFullTimeEntity.home ?: 0
    }
}

fun MatchesEntity.toCompetitionMatches(): Matches {
    return Matches(
        id = id,
        matchesByTourCompleted = filterCompletedMatches(this),
        matchesByTourAhead = filterAheadMatches(this)
    )
}

fun MatchesEntity.toTeamMatches(): TeamMatches {
    return TeamMatches(
        id = id,
        matchesCompleted = groupMatchesByDateCompleted(this.matches ?: emptyList()),
        matchesAhead = groupMatchesByDateAhead(this.matches ?: emptyList())
    )
}

fun groupMatchesByDateCompleted(matches: List<MatchEntity>): List<Match> {
    return matches.filter { it.status == "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
        .sortedByDescending { it.utcDate }.map { it.toMatches() }
}

fun groupMatchesByDateAhead(matches: List<MatchEntity>): List<Match> {
    return matches.filter { it.status != "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
        .sortedBy { it.utcDate }.map { it.toMatches() }
}


fun filterAheadMatches(matches: MatchesEntity): List<MatchesByTour> {
    val filteredMatches = matches.matches?.filter {
        it.status != "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null
    } ?: emptyList()

    return groupMatchesByTour(
        matches = filteredMatches,
        seasonType = matches.seasonType,
        ascending = true
    )
}

fun filterCompletedMatches(matches: MatchesEntity): List<MatchesByTour> {
    val filteredMatches = matches.matches?.filter {
        it.status == "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null
    } ?: emptyList()

    return groupMatchesByTour(
        matches = filteredMatches,
        seasonType = matches.seasonType,
        ascending = false
    )
}

private fun groupMatchesByTour(
    matches: List<MatchEntity>,
    seasonType: String,
    ascending: Boolean
): List<MatchesByTour> {
    return matches
        .groupBy { it.stage to it.matchDay }
        .map { (key, tourMatches) ->
            val (stage, matchDay) = key
            val sortedMatches = if (ascending) {
                tourMatches.sortedBy { it.utcDate }
            } else {
                tourMatches.sortedByDescending { it.utcDate }
            }
            val groupSortKey = sortedMatches.firstOrNull()?.utcDate.orEmpty()
            groupSortKey to MatchesByTour(
                matchDay = matchDay,
                stage = Stage.safeValueOf(stage),
                seasonType = TournamentType.safeValueOf(seasonType),
                matches = sortedMatches.map { it.toMatches() }
            )
        }
        .let { tours ->
            if (ascending) {
                tours.sortedBy { it.first }
            } else {
                tours.sortedByDescending { it.first }
            }
        }
        .map { it.second }
}

fun MatchEntity.toMatches(): Match {
    return Match(
        area = area.toArea(),
        competition = competition.toCompetition(),
        awayTeam = awayTeam.toMatchTeam(),
        group = Group.safeValueOf(group),
        homeTeam = homeTeam.toMatchTeam(),
        id = id,
        matchDay = matchDay,
        referees = referees?.map { it.toReferee() } ?: emptyList(),
        score = score.toScore(),
        stage = Stage.safeValueOf(stage),
        status = MatchStatus.safeValueOf(status),
        utcDate = utcDate.toDate()?.formatTo("dd MMMM yyyy, HH:mm") ?: "",
        bigDate = utcDate.toDate()?.formatTo("dd.MM") ?: ""
    )
}

fun TeamEmbeddedEntity?.toMatchTeam(): MatchTeam {
    return MatchTeam(
        id = this?.id ?: UUID.randomUUID().hashCode(),
        crest = this?.crest ?: "",
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}

fun RefereeEntity.toReferee(): Referee {
    return Referee(
        id = id,
        name = name,
        nationality = nationality,
        type = type
    )
}

fun ScoreEntity?.toScore(): Score {
    return Score(
        duration = this?.duration ?: "",
        winner = Winner.safeValueOf(this?.winner ?: ""),
        fullTime = this?.fullTime.toTime(),
        halfTime = this?.halfTime.toTime()
    )
}

fun TimeEntity?.toTime(): Time {
    return Time(
        away = this?.away ?: -1,
        home = this?.home ?: -1
    )
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ssX",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.US)
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(
    dateFormat: String,
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Karachi")
): String { //todo correct time zone
    val formatter = SimpleDateFormat(dateFormat, Locale.US) // todo locale
    formatter.timeZone = timeZone
    return formatter.format(this)
}