package ru.asmelnikov.data.mappers

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.data.models.PersonDTO
import java.time.LocalDate

class TeamInfoMappersTest {

    @Test
    fun convertToRealmList_groupsPlayersByPosition() {
        val squad = listOf(
            person(id = 1, name = "Saliba", position = "Defence"),
            person(id = 2, name = "Raya", position = "Goalkeeper"),
            person(id = 3, name = "Gabriel", position = "Defence"),
            person(id = 4, name = "Unknown", position = null)
        )

        assertEquals(
            listOf(
                "Defence" to listOf("Saliba", "Gabriel"),
                "Goalkeeper" to listOf("Raya"),
                "" to listOf("Unknown")
            ),
            convertToRealmList(squad).map { group ->
                group.position to group.squad.orEmpty().map { it.name }
            }
        )
    }

    @Test
    fun calculateAge_returnsYearsFromDateOfBirth() {
        val dateOfBirth = LocalDate.now().minusYears(25).minusMonths(1).toString()

        assertEquals("25", dateOfBirth.calculateAge())
    }

    @Test
    fun calculateAge_returnsEmpty_whenDateIsInvalid() {
        assertEquals("", "15-08-2000".calculateAge())
        assertEquals("", "".calculateAge())
    }

    // region Factories

    private fun person(id: Int, name: String, position: String?): PersonDTO {
        return PersonDTO(
            id = id,
            contract = null,
            dateOfBirth = null,
            firstName = null,
            lastName = null,
            name = name,
            nationality = null,
            position = position,
            shirtNumber = null
        )
    }

    // endregion
}
