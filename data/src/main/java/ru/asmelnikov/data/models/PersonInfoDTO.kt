package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonInfoDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "currentTeam") val currentTeam: TeamInfoDTO?,
    @Json(name = "dateOfBirth") val dateOfBirth: String?,
    @Json(name = "firstName") val firstName: String?,
    @Json(name = "lastName") val lastName: String?,
    @Json(name = "lastUpdated") val lastUpdated: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "nationality") val nationality: String?,
    @Json(name = "position") val position: String?,
    @Json(name = "section") val section: String?,
    @Json(name = "shirtNumber") val shirtNumber: Int?,
    @Json(name = "contract") val contract: ContractDTO?
)
