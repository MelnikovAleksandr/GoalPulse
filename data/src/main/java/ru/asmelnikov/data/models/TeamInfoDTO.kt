package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamInfoDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "address") val address: String?,
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "clubColors") val clubColors: String?,
    @Json(name = "coach") val coach: PersonDTO?,
    @Json(name = "crest") val crest: String?,
    @Json(name = "founded") val founded: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "shortName") val shortName: String?,
    @Json(name = "squad") val squad: List<PersonDTO>?,
    @Json(name = "tla") val tla: String?,
    @Json(name = "venue") val venue: String?,
    @Json(name = "website") val website: String?,
)

@JsonClass(generateAdapter = true)
data class PersonDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "contract") val contract: ContractDTO?,
    @Json(name = "dateOfBirth") val dateOfBirth: String?,
    @Json(name = "firstName") val firstName: String?,
    @Json(name = "lastName") val lastName: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "nationality") val nationality: String?,
    @Json(name = "position") val position: String?,
    @Json(name = "shirtNumber") val shirtNumber: Int?,
)

@JsonClass(generateAdapter = true)
data class ContractDTO(@Json(name = "start") val start: String?, @Json(name = "until") val until: String?)
