package ru.asmelnikov.data.mappers

//fun MatchesDTO.toCompetitionMatches(): CompetitionMatches {
//    return CompetitionMatches(
//        id = this.competition?.id.toString(),
//        season = createYearRange(
//            this.matches?.firstOrNull()?.season?.startDate ?: "",
//            this.matches?.firstOrNull()?.season?.endDate ?: ""
//        ),
//        matchesByTourCompleted = groupMatchesByTourCompleted(matches ?: emptyList()),
//        matchesByTourAhead = groupMatchesByTourAhead(matches ?: emptyList())
//    )
//}
//
//fun groupMatchesByTourCompleted(matches: List<MatchDTO>): List<MatchesByTour> {
//    val groupedMatches = matches.groupBy { it.stage }
//    val result = mutableListOf<MatchesByTour>()
//
//    groupedMatches.forEach { (stage, matchesByStage) ->
//        val matchesByTour = matchesByStage.groupBy { it.matchDay }
//        matchesByTour.forEach { (matchday, matchesByMatchday) ->
//            val completedMatches =
//                matchesByMatchday.filter { it.status == "FINISHED" }.map { it.toMatch() }
//            if (completedMatches.isNotEmpty()) {
//                result.add(
//                    MatchesByTour(
//                        matchday ?: -1,
//                        matches.firstOrNull()?.stage ?: "",
//                        stage,
//                        completedMatches
//                    )
//                )
//            }
//        }
//    }
//    return result.reversed()
//}
//
//fun groupMatchesByTourAhead(matches: List<MatchDTO>): List<MatchesByTour> {
//    val groupedMatches = matches.groupBy { it.stage }
//    val result = mutableListOf<MatchesByTour>()
//
//    groupedMatches.forEach { (stage, matchesByStage) ->
//        val matchesByTour = matchesByStage.groupBy { it.matchDay }
//        matchesByTour.forEach { (matchday, matchesByMatchday) ->
//            val aheadMatches =
//                matchesByMatchday.filter { it.status != "FINISHED" }.map { it.toMatch() }
//            if (aheadMatches.isNotEmpty()) {
//                result.add(
//                    MatchesByTour(
//                        matchday ?: -1,
//                        matches.firstOrNull()?.stage ?: "",
//                        stage,
//                        aheadMatches
//                    )
//                )
//            }
//        }
//    }
//
//    return result
//}
//
//fun MatchDTO.toMatch(): Match {
//    val dateTime = LocalDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME)
//    val zoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(5))
//    val zonedDateTime = dateTime.atZone(zoneId)
//    val outputDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"))
//    val bigOutputDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
//    return Match(
//        awayTeam = awayTeam.toMatchTeam(),
//        group = group ?: "",
//        homeTeam = homeTeam.toHomeTeam(),
//        id = id ?: -1,
//        lastUpdated = lastUpdated ?: "",
//        matchDay = matchDay ?: -1,
//        referees = referees?.map { it.toReferee() } ?: emptyList(),
//        score = score.toScore(),
//        stage = stage ?: "",
//        status = status ?: "",
//        utcDate = outputDateTime ?: "",
//        bigDate = bigOutputDateTime ?: "",
//        area = area.toArea(),
//        competition = competition.toCompetition()
//    )
//}
//
//fun RefereeDTO.toReferee(): Referee {
//    return Referee(
//        id = id ?: -1,
//        name = name ?: "",
//        nationality = nationality ?: "",
//        type = type ?: ""
//    )
//}
//
//fun ScoreDTO?.toScore(): Score {
//    return Score(
//        duration = this?.duration ?: "",
//        winner = this?.winner ?: "",
//        fullTime = this?.fullTime.toTime(),
//        halfTime = this?.halfTime.toHalfTime(),
//    )
//}
//
//fun TimeDTO?.toTime(): Time {
//    return Time(
//        away = this?.away ?: -1,
//        home = this?.away ?: -1
//    )
//}
//
//fun HalfTimeDTO?.toHalfTime(): HalfTime {
//    return HalfTime(
//        away = this?.away ?: -1,
//        home = this?.away ?: -1
//    )
//}