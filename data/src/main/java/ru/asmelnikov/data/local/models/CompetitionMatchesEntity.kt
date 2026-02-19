package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CompetitionMatchesEntity(
    @PrimaryKey
    var id: String = "",
    var season: String = "",
    var seasonType: String = "",
    var matchesByTourCompleted: RealmList<MatchesByTourEntity>? = null,
    var matchesByTourAhead: RealmList<MatchesByTourEntity>? = null,
) : RealmObject {
    constructor() : this("")
}

class MatchesByTourEntity(
    var matchday: Int? = -1,
    var stage: String? = "",
    var matches: RealmList<MatchEntity>? = null
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class MatchEntity(
    var id: Int? = -1,
    var area: AreaEntity? = null,
    var competition: CompetitionEntity? = null,
    var awayTeam: AwayTeamEntity? = null,
    var group: String? = "",
    var homeTeam: HomeTeamEntity? = null,
    var lastUpdated: String? = "",
    var matchday: Int? = -1,
    var referees: RealmList<RefereeEntity>? = null,
    var score: ScoreEntity? = null,
    var stage: String? = "",
    var status: String? = "",
    var utcDate: String? = ""
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class AwayTeamEntity(
    var id: Int? = -1,
    var crest: String? = "",
    var name: String? = "",
    var shortName: String? = "",
    var tla: String? = ""
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class HomeTeamEntity(
    var id: Int? = -1,
    var crest: String? = "",
    var name: String? = "",
    var shortName: String? = "",
    var tla: String? = ""
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class RefereeEntity(
    var id: Int? = -1,
    var name: String? = "",
    var nationality: String? = "",
    var type: String? = ""
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class ScoreEntity(
    var duration: String? = "",
    var fullTime: FullTimeEntity? = null,
    var halfTime: HalfTimeEntity? = null,
    var winner: String? = ""
) : EmbeddedRealmObject {
    constructor() : this("")
}

class FullTimeEntity(
    var away: Int? = -1,
    var home: Int? = -1
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class HalfTimeEntity(
    var away: Int? = -1,
    var home: Int? = -1
) : EmbeddedRealmObject {
    constructor() : this(0)
}
