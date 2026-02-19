package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CompetitionEntity(
    @PrimaryKey
    var id: Int,
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
) : RealmObject {
    constructor() : this(0)
}


class AreaEntity(
    var id: Int = -1,
    var code: String = "",
    var flag: String = "",
    var name: String = ""
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class CurrentSeasonEntity(
    var id: Int = -1,
    var currentMatchDay: Int = -1,
    var endDate: String = "",
    var startDate: String = "",
    var winner: WinnerEntity? = null
) : EmbeddedRealmObject {
    constructor() : this(0)
}

class WinnerEntity(
    var id: Int = -1,
    var address: String = "",
    var clubColors: String = "",
    var crest: String = "",
    var founded: Int = -1,
    var lastUpdated: String = "",
    var name: String = "",
    var shortName: String = "",
    var tla: String = "",
    var website: String = "",
    var venue: String = ""
) : EmbeddedRealmObject{
    constructor() : this(0)
}