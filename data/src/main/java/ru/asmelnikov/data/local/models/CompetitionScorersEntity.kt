package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CompetitionScorersEntity(
    @PrimaryKey
    var id: String = "",
    var season: SeasonEntity? = null,
    var scorers: RealmList<ScorerEntity>? = null
) : RealmObject {
    constructor() : this("")
}

class ScorerEntity(
    var assists: Int? = -1,
    var goals: Int? = -1,
    var penalties: Int? = -1,
    var playedMatches: Int? = -1,
    var player: PlayerEntity? = null,
    var team: TeamEntity? = null
) : EmbeddedRealmObject {
    constructor() : this(-1)
}

class PlayerEntity(
    var id: Int? = -1,
    var dateOfBirth: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var lastUpdated: String? = "",
    var name: String? = "",
    var nationality: String? = "",
    var position: String? = "",
    var section: String? = "",
    var shirtNumber: Int? = -1
) : EmbeddedRealmObject {
    constructor() : this(-1)
}