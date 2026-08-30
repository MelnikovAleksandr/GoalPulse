package ru.asmelnikov.data.mappers

import io.realm.kotlin.ext.realmListOf
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.ScorerEntity

class CompetitionsScorersMappersTest {

    @Test
    fun toCompetitionScorers_sortsByGoalsThenAssistsDescending() {
        val entity = scorersEntity(
            listOf(
                scorer(name = "Salah", goals = 25, assists = 10),
                scorer(name = "Haaland", goals = 30, assists = 5),
                scorer(name = "Palmer", goals = 25, assists = 12),
                scorer(name = "Kane", goals = 30, assists = 8),
            ),
        )

        val scorers = entity.toCompetitionScorers().scorers

        assertEquals(
            listOf(
                Triple("Kane", 30, 8),
                Triple("Haaland", 30, 5),
                Triple("Palmer", 25, 12),
                Triple("Salah", 25, 10),
            ),
            scorers.map { Triple(it.player.name, it.goals, it.assists) },
        )
    }

    // region Factories

    private fun scorersEntity(scorers: List<ScorerEntity>): CompetitionScorersEntity {
        val entity = CompetitionScorersEntity()
        entity.scorers = realmListOf<ScorerEntity>().apply { addAll(scorers) }
        return entity
    }

    private fun scorer(name: String, goals: Int, assists: Int): ScorerEntity {
        val entity = ScorerEntity()
        entity.goals = goals
        entity.assists = assists
        entity.player = PlayerEntity().also { it.name = name }
        return entity
    }

    // endregion
}
