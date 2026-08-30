package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class CompetitionStandingsEntity : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var area: AreaEntity? = null
    var competition: CompetitionEmbeddedEntity? = null
    var standings: RealmList<StandingEntity>? = null
}

class StandingEntity : EmbeddedRealmObject {
    var group: String = ""
    var stage: String = ""
    var table: RealmList<TableEntity>? = null
    var type: String = ""
}

class TableEntity : EmbeddedRealmObject {
    var draw: Int = -1
    var form: String = ""
    var goalDifference: Int = -1
    var goalsAgainst: Int = -1
    var goalsFor: Int = -1
    var lost: Int = -1
    var playedGames: Int = -1
    var points: Int = -1
    var position: Int = -1
    var team: TeamEmbeddedEntity? = null
    var won: Int = -1
}

class CompetitionEmbeddedEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var area: AreaEntity? = null
    var code: String = ""
    var currentSeason: CurrentSeasonEntity? = null
    var emblem: String = ""
    var name: String = ""
    var type: String = ""
}
