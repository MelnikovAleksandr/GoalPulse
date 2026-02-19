package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CompetitionStandingsEntity(
    @PrimaryKey
    var id: String = "",
    var area: AreaEntity? = null,
    var competition: CompetitionEmbeddedEntity? = null,
    var filters: FiltersEntity? = null,
    var season: SeasonEntity? = null,
    var standings: RealmList<StandingEntity>? = null
) : RealmObject {
    constructor() : this("")
}

class CompetitionEmbeddedEntity(
    var id: Int = -1,
    var area: AreaEntity? = null,
    var code: String = "",
    var currentSeason: CurrentSeasonEntity? = null,
    var emblem: String = "",
    var lastUpdated: String = "",
    var name: String = "",
    var numberOfAvailableSeasons: Int = -1,
    var plan: String = "",
    var type: String = "",
    var seasons: RealmList<SeasonEntity>? = null
) : EmbeddedRealmObject {
    constructor() : this(-1)
}

class FiltersEntity(
    var season: String = ""
) : EmbeddedRealmObject {
    constructor() : this("")
}

class SeasonEntity(
    var id: Int = -1,
    var currentMatchday: Int = -1,
    var endDate: String = "",
    var startDate: String = "",
    var winner: WinnerEntity? = null
) : EmbeddedRealmObject {
    constructor() : this(-1)
}

class StandingEntity(
    var group: String = "",
    var stage: String = "",
    var table: RealmList<TableEntity>? = null,
    var type: String = ""
) : EmbeddedRealmObject {
    constructor() : this("")
}

class TableEntity(
    var draw: Int = -1,
    var form: String = "",
    var goalDifference: Int = -1,
    var goalsAgainst: Int = -1,
    var goalsFor: Int = -1,
    var lost: Int = -1,
    var playedGames: Int = -1,
    var points: Int = -1,
    var position: Int = -1,
    var team: TeamEntity? = null,
    var won: Int = -1
) : EmbeddedRealmObject {
    constructor() : this(-1)
}

class TeamEntity(
    var id: Int = -1,
    var crest: String = "",
    var name: String = "",
    var shortName: String = "",
    var tla: String = ""
) : EmbeddedRealmObject {
    constructor() : this(-1)
}