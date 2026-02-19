package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TeamMatchesEntity(
    @PrimaryKey
    var id: String = "",
    var season: String = "",
    var matchesCompleted: RealmList<MatchEntity>? = null,
    var matchesAhead: RealmList<MatchEntity>? = null,
) : RealmObject {
    constructor() : this("")
}
