package ru.asmelnikov.data.mappers

import java.util.UUID
import ru.asmelnikov.data.models.AreaDTO
import ru.asmelnikov.data.models.PersonInfoDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.domain.models.Area
import ru.asmelnikov.domain.models.CurrentTeam
import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.domain.models.PlayerPosition

fun PersonInfoDTO.toPerson(): Person = Person(
    id = id ?: UUID.randomUUID().hashCode(),
    currentTeam = currentTeam.toCurrentTeam(),
    age = dateOfBirth?.calculateAge() ?: "",
    firstName = firstName ?: "",
    lastName = lastName ?: "",
    lastUpdated = lastUpdated ?: "",
    name = name ?: "",
    nationality = nationality ?: "",
    position = PlayerPosition.fromValue(position),
    section = section ?: "",
    shirtNumber = shirtNumber ?: 0,
)

fun TeamInfoDTO?.toCurrentTeam(): CurrentTeam = CurrentTeam(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    address = this?.address ?: "",
    area = this?.area.toArea(),
    clubColors = this?.clubColors ?: "",
    crest = this?.crest ?: "",
    founded = this?.founded ?: -1,
    name = this?.name ?: "",
    shortName = this?.shortName ?: "",
    tla = this?.tla ?: "",
    venue = this?.venue ?: "",
    website = this?.website ?: "",
)

fun AreaDTO?.toArea(): Area = Area(
    id = this?.id ?: UUID.randomUUID().hashCode(),
    flag = this?.flag ?: "",
    name = this?.name ?: "",
)
