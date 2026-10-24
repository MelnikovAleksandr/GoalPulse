package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.data.local.models.StandingEntity
import ru.asmelnikov.data.local.models.TableEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.data.models.StandingDTO

class CompetitionStandingsMappersTest {

    @Test
    fun toStanding_sortsTableByPositionAscending() {
        val standing = standingEntity(
            listOf(
                tableRow(position = 3, teamName = "Chelsea"),
                tableRow(position = 1, teamName = "Arsenal"),
                tableRow(position = 2, teamName = "City"),
            ),
        )

        assertEquals(
            listOf(
                1 to "Arsenal",
                2 to "City",
                3 to "Chelsea",
            ),
            standing.toStanding().table.map { it.position to it.team.name },
        )
    }

    @Test
    fun toCompetitionStandingsEntity_keepsOnlyTotalTable() {
        val dto = CompetitionStandingsModelDTO(
            area = null,
            competition = null,
            season = null,
            standings = listOf("TOTAL", "HOME", "AWAY").map { type ->
                StandingDTO(group = null, stage = "GROUP_STAGE", table = emptyList(), type = type)
            },
        )

        assertEquals(
            listOf("TOTAL"),
            dto.toCompetitionStandingsEntity().standings?.map { it.type },
        )
    }

    // region Factories

    private fun standingEntity(table: List<TableEntity>): StandingEntity {
        val entity = StandingEntity()
        entity.table = realmListOf<TableEntity>().apply { addAll(table) }
        return entity
    }

    private fun tableRow(position: Int, teamName: String): TableEntity {
        val entity = TableEntity()
        entity.position = position
        entity.team = TeamEmbeddedEntity().also { it.name = teamName }
        return entity
    }

    // endregion
}
