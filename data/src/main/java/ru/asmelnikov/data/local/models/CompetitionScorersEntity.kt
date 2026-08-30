package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class CompetitionScorersEntity : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var season: CurrentSeasonEntity? = null
    var scorers: RealmList<ScorerEntity>? = null
}

class ScorerEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var assists: Int = -1
    var goals: Int = -1
    var penalties: Int = -1
    var playedMatches: Int = -1
    var player: PlayerEntity? = null
    var team: TeamEmbeddedEntity? = null
}

class PlayerEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var dateOfBirth: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var name: String = ""
    var nationality: String = ""
    var position: String = ""
    var shirtNumber: Int? = -1
}
