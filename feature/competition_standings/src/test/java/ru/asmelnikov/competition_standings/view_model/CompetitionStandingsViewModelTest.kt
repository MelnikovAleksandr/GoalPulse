package ru.asmelnikov.competition_standings.view_model

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.orbitmvi.orbit.test.test
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.Matches
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Player
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.Standing
import ru.asmelnikov.domain.models.Table
import ru.asmelnikov.domain.models.Team
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider

class CompetitionStandingsViewModelTest {

    @Test
    fun onOpen_showsStandingsScorersAndMatchesForOpenedCompetition() = runTest {
        val repository = FakeStandingsRepository(
            standingsById = mapOf("2021" to premierLeagueStandings),
            scorersById = mapOf("2021" to premierLeagueScorers),
            matchesById = mapOf("2021" to premierLeagueMatches)
        )

        viewModel(repository).test(this) {
            runOnCreate()

            var screen = awaitState()
            while (
                screen.competitionStandings.competition.name != "Premier League" ||
                screen.isLoadingStandings ||
                screen.scorers.none { it.player.name == "Bukayo Saka" } ||
                screen.matchesCompleted.none { tour -> tour.matches.any { it.id == COMPLETED_MATCH_ID } }
            ) {
                screen = awaitState()
            }

            assertEquals("Premier League", screen.competitionStandings.competition.name)
            assertEquals("2021", screen.competitionStandings.id)
            assertEquals(PL_CREST, screen.compUrl)
            assertEquals(listOf("Bukayo Saka"), screen.scorers.map { it.player.name })
            assertEquals(
                listOf(COMPLETED_MATCH_ID),
                screen.matchesCompleted.flatMap { tour -> tour.matches.map { it.id } }
            )

            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun standingsLoadFails_hidesSpinner_showsError() = runTest {
        val repository = FakeStandingsRepository(
            standingsError = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(this, CompetitionStandingsState(compId = "2021")) {
            containerHost.updateStandingsFromRemoteToLocal()

            expectState { copy(isLoadingStandings = false) }
            expectSideEffect(CompetitionStandingSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun standingsReloadFails_keepsAlreadyLoadedTable_showsError() = runTest {
        val repository = FakeStandingsRepository(
            standingsError = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(
            this,
            CompetitionStandingsState(
                compId = "2021",
                competitionStandings = premierLeagueStandings,
                isLoadingStandings = false
            )
        ) {
            containerHost.updateStandingsFromRemoteToLocal()

            expectState { copy(isLoadingStandings = true) }
            expectState { copy(isLoadingStandings = false) }
            expectSideEffect(CompetitionStandingSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun scorersLoadFails_hidesSpinner_showsError() = runTest {
        val repository = FakeStandingsRepository(
            scorersError = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(this, CompetitionStandingsState(compId = "2021")) {
            containerHost.updateScorersFromRemoteToLocal()

            expectState { copy(isLoadingScorers = false) }
            expectSideEffect(CompetitionStandingSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun matchesLoadFails_hidesSpinner_showsError() = runTest {
        val repository = FakeStandingsRepository(
            matchesError = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(this, CompetitionStandingsState(compId = "2021")) {
            containerHost.updateMatchesFromRemoteToLocal()

            expectState { copy(isLoadingMatches = false) }
            expectSideEffect(CompetitionStandingSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun clickMatch_loadsHead2headForThatMatch() = runTest {
        val repository = FakeStandingsRepository(
            head2headById = mapOf(COMPLETED_MATCH_ID to match100Head2head)
        )

        viewModel(repository).test(
            this,
            CompetitionStandingsState(
                compId = "2021",
                isLoadingStandings = false,
                isLoadingScorers = false,
                isLoadingMatches = false
            )
        ) {
            containerHost.matchItemClick(COMPLETED_MATCH_ID)

            expectState { copy(expandedItem = COMPLETED_MATCH_ID, isHead2headLoading = true) }
            expectState { copy(head2head = match100Head2head, isHead2headLoading = false) }
        }
    }

    @Test
    fun clickExpandedMatch_collapsesIt() = runTest {
        viewModel().test(
            this,
            CompetitionStandingsState(
                compId = "2021",
                expandedItem = COMPLETED_MATCH_ID,
                head2head = match100Head2head,
                isLoadingStandings = false,
                isLoadingScorers = false,
                isLoadingMatches = false
            )
        ) {
            containerHost.matchItemClick(COMPLETED_MATCH_ID)

            expectState { copy(expandedItem = -1) }
        }
    }

    @Test
    fun clickAnotherMatch_loadsThatMatchHead2headNotPrevious() = runTest {
        val repository = FakeStandingsRepository(
            head2headById = mapOf(
                COMPLETED_MATCH_ID to match100Head2head,
                OTHER_MATCH_ID to match200Head2head
            )
        )

        viewModel(repository).test(
            this,
            CompetitionStandingsState(
                compId = "2021",
                expandedItem = COMPLETED_MATCH_ID,
                head2head = match100Head2head,
                isLoadingStandings = false,
                isLoadingScorers = false,
                isLoadingMatches = false
            )
        ) {
            containerHost.matchItemClick(OTHER_MATCH_ID)

            expectState { copy(expandedItem = OTHER_MATCH_ID, isHead2headLoading = true) }
            expectState { copy(head2head = match200Head2head, isHead2headLoading = false) }
        }
    }

    @Test
    fun clickMatch_whenHead2headFails_hidesSpinner_showsError() = runTest {
        val repository = FakeStandingsRepository(
            head2headError = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(
            this,
            CompetitionStandingsState(
                compId = "2021",
                isLoadingStandings = false,
                isLoadingScorers = false,
                isLoadingMatches = false
            )
        ) {
            containerHost.matchItemClick(COMPLETED_MATCH_ID)

            expectState { copy(expandedItem = COMPLETED_MATCH_ID, isHead2headLoading = true) }
            expectState { copy(isHead2headLoading = false) }
            expectSideEffect(CompetitionStandingSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    // region Helpers

    private fun viewModel(
        repository: FakeStandingsRepository = FakeStandingsRepository()
    ) = CompetitionStandingsViewModel(
        standingsRepository = repository,
        stringResourceProvider = FakeStringResourceProvider(),
        compId = "2021",
        compUrl = PL_CREST,
        savedStateHandle = SavedStateHandle()
    )

    // endregion
}

// region Fakes

private class FakeStandingsRepository(
    private val standingsById: Map<String, CompetitionStandings> = emptyMap(),
    private val scorersById: Map<String, CompetitionScorers> = emptyMap(),
    private val matchesById: Map<String, Matches> = emptyMap(),
    private val head2headById: Map<Int, Head2head> = emptyMap(),
    private val standingsError: ErrorsTypesHttp? = null,
    private val scorersError: ErrorsTypesHttp? = null,
    private val matchesError: ErrorsTypesHttp? = null,
    private val head2headError: ErrorsTypesHttp? = null
) : CompetitionStandingsRepository {

    override suspend fun getCompetitionStandingsFromRemoteToLocalById(compId: String): Resource<Boolean> {
        if (standingsError != null) {
            return Resource.Error(httpErrors = standingsError)
        }
        return Resource.Success(true)
    }

    override suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings?> {
        return flowOf(standingsById[compId])
    }

    override suspend fun getCompetitionTopScorersBySeason(compId: String): Resource<Boolean> {
        if (scorersError != null) {
            return Resource.Error(httpErrors = scorersError)
        }
        return Resource.Success(true)
    }

    override suspend fun getScorersFlowFromLocal(compId: String): Flow<CompetitionScorers?> {
        return flowOf(scorersById[compId])
    }

    override suspend fun getAllMatchesFromRemoteToLocal(compId: String): Resource<Boolean> {
        if (matchesError != null) {
            return Resource.Error(httpErrors = matchesError)
        }
        return Resource.Success(true)
    }

    override suspend fun getAllMatchesFlowFromLocal(compId: String): Flow<Matches?> {
        return flowOf(matchesById[compId])
    }

    override suspend fun getHead2headById(matchId: Int): Resource<Head2head> {
        if (head2headError != null) {
            return Resource.Error(httpErrors = head2headError)
        }
        val head2head = head2headById[matchId]
        return if (head2head != null) {
            Resource.Success(head2head)
        } else {
            Resource.Success(Head2head())
        }
    }
}

private class FakeStringResourceProvider : StringResourceProvider {
    override fun getString(resourceId: Int): String {
        return when (resourceId) {
            R.string.http_429_errors -> RATE_LIMIT_MESSAGE
            else -> error("unexpected string resource $resourceId")
        }
    }

    override fun getString(resourceId: Int, vararg arguments: Any): String {
        error("unexpected formatted string resource $resourceId")
    }
}

// endregion

// region Test data

private const val RATE_LIMIT_MESSAGE = "too many requests"
private const val COMPLETED_MATCH_ID = 100
private const val OTHER_MATCH_ID = 200
private const val PL_CREST = "https://crests.football-data.org/PL.png"

private val premierLeagueStandings = CompetitionStandings(
    id = "2021",
    competition = Competition(id = 2021, name = "Premier League"),
    standings = listOf(
        Standing(
            table = listOf(
                Table(position = 1, team = Team(id = 57, name = "Arsenal"))
            )
        )
    )
)

private val premierLeagueScorers = CompetitionScorers(
    id = "2021",
    scorers = listOf(
        Scorer(goals = 15, player = Player(id = 44, name = "Bukayo Saka"))
    )
)

private val premierLeagueMatches = Matches(
    id = "2021",
    matchesByTourCompleted = listOf(
        MatchesByTour(matchDay = 1, matches = listOf(Match(id = COMPLETED_MATCH_ID)))
    )
)

private val match100Head2head = Head2head(id = COMPLETED_MATCH_ID)
private val match200Head2head = Head2head(id = OTHER_MATCH_ID)

// endregion
