package ru.asmelnikov.data.local.models

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class TeamInfoEntity : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var address: String = ""
    var area: AreaEntity? = null
    var clubColors: String = ""
    var coach: PersonEntity? = null
    var crest: String = ""
    var founded: Int = -1
    var name: String = ""
    var shortName: String = ""
    var squadByPosition: RealmList<SquadByPositionEntity>? = null
    var tla: String = ""
    var venue: String = ""
    var website: String = ""
}

class PersonEntity : EmbeddedRealmObject {
    var id: Int = UUID.randomUUID().hashCode()
    var contract: ContractEntity? = null
    var dateOfBirth: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var name: String = ""
    var nationality: String = ""
}

class SquadByPositionEntity : EmbeddedRealmObject {
    var position: String = ""
    var squad: RealmList<PersonEntity>? = null
}

class ContractEntity : EmbeddedRealmObject {
    var start: String = ""
    var until: String = ""
}
