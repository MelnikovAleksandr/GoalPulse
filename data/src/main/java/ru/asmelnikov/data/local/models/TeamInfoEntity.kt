package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TeamInfoEntity(
    @PrimaryKey
    var id: String = "",
    var address: String = "",
    var area: AreaEntity? = null,
    var clubColors: String = "",
    var coach: CoachEntity? = null,
    var crest: String = "",
    var founded: Int = -1,
    var lastUpdated: String = "",
    var name: String = "",
    var shortName: String = "",
    var squadByPosition: RealmList<SquadByPositionEntity>? = null,
    var tla: String = "",
    var venue: String = "",
    var website: String = ""
) : RealmObject {
    constructor() : this("")
}

class CoachEntity(
    var id: Int = -1,
    var contract: ContractEntity? = null,
    var dateOfBirth: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var name: String = "",
    var nationality: String = ""
) : EmbeddedRealmObject {
    constructor() : this(-1)
}

class SquadByPositionEntity(
    var position: String = "",
    var squad: RealmList<SquadEntity>? = null
) : EmbeddedRealmObject {
    constructor() : this("")
}

class SquadEntity(
    var id: Int = -1,
    var dateOfBirth: String = "",
    var name: String = "",
    var nationality: String = ""
) : EmbeddedRealmObject {
    constructor() : this(-1)
}

class ContractEntity(
    var start: String = "",
    var until: String = ""
) : EmbeddedRealmObject {
    constructor() : this("")
}