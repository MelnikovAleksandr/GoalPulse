package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class MatchesEntity : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var seasonType: String = ""
    var matches: RealmList<MatchEntity>? = null
}

class MatchEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var area: AreaEntity? = null
    var competition: CompetitionEmbeddedEntity? = null
    var awayTeam: TeamEmbeddedEntity? = null
    var group: String = ""
    var homeTeam: TeamEmbeddedEntity? = null
    var matchDay: Int = -1
    var referees: RealmList<RefereeEntity>? = null
    var score: ScoreEntity? = null
    var stage: String = ""
    var status: String = ""
    var utcDate: String = ""
}

class RefereeEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var name: String = ""
    var nationality: String = ""
    var type: String = ""
}

class ScoreEntity : EmbeddedRealmObject {
    var duration: String = ""
    var fullTime: TimeEntity? = null
    var halfTime: TimeEntity? = null
    var winner: String = ""
}

class TimeEntity : EmbeddedRealmObject {
    var away: Int = -1
    var home: Int = -1
}
