package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class CompetitionEntity : RealmObject {
    @PrimaryKey
    var id: Int = UUID.randomUUID().hashCode()
    var area: AreaEntity? = null
    var code: String = ""
    var currentSeason: CurrentSeasonEntity? = null
    var emblem: String = ""
    var name: String = ""
    var type: String = ""
}


class AreaEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var code: String = ""
    var flag: String = ""
    var name: String = ""
}

class CurrentSeasonEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var currentMatchDay: Int = -1
    var endDate: String = ""
    var startDate: String = ""
    var winner: TeamEmbeddedEntity? = null
}

class TeamEmbeddedEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var address: String = ""
    var clubColors: String = ""
    var crest: String = ""
    var founded: Int = -1
    var lastUpdated: String = ""
    var name: String = ""
    var shortName: String = ""
    var tla: String = ""
    var website: String = ""
    var venue: String = ""
}