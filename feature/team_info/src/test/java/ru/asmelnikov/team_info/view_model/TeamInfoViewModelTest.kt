package ru.asmelnikov.team_info.view_model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.orbitmvi.orbit.test.test
import ru.asmelnikov.domain.models.Article
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.TeamMatches
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.NewsRepository
import ru.asmelnikov.domain.repository.TeamInfoRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider

class TeamInfoViewModelTest {

    @Test
    fun onOpen_showsTeamMatchesAndNewsForOpenedId() = runTest {
        val teamRepository = FakeTeamInfoRepository(
            teamById = mapOf("57" to arsenal),
            matchesById = mapOf("57" to arsenalMatches)
        )
        val newsRepository = FakeNewsRepository(
            newsByQuery = mapOf("Arsenal FC" to arsenalNews)
        )

        viewModel(teamRepository, newsRepository).test(this) {
            runOnCreate()

            var screen = awaitState()
            while (
                screen.teamInfo.name != "Arsenal FC" ||
                screen.isInfoLoading ||
                screen.matchesComplete.none { it.id == COMPLETED_MATCH_ID } ||
                screen.news.articles.none { it.title == ARSENAL_NEWS_TITLE }
            ) {
                screen = awaitState()
            }

            assertEquals("Arsenal FC", screen.teamInfo.name)
            assertEquals("57", screen.teamInfo.id)
            assertEquals(listOf(COMPLETED_MATCH_ID), screen.matchesComplete.map { it.id })
            assertEquals(listOf(ARSENAL_NEWS_TITLE), screen.news.articles.map { it.title })

            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun loadFails_hidesSpinner_showsError() = runTest {
        val repository = FakeTeamInfoRepository(
            error = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(this, TeamInfoState(teamId = "57")) {
            containerHost.getTeamInfoFromRemoteToLocal()

            expectState { copy(isInfoLoading = false) }
            expectSideEffect(TeamInfoSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun reloadFails_keepsAlreadyLoadedTeam_showsError() = runTest {
        val repository = FakeTeamInfoRepository(
            error = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(
            this,
            TeamInfoState(teamId = "57", teamInfo = arsenal, isInfoLoading = false)
        ) {
            containerHost.getTeamInfoFromRemoteToLocal()

            expectState { copy(isInfoLoading = true) }
            expectState { copy(isInfoLoading = false) }
            expectSideEffect(TeamInfoSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun matchesLoadFails_hidesSpinner_showsError() = runTest {
        val repository = FakeTeamInfoRepository(
            matchesError = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(repository).test(this, TeamInfoState(teamId = "57")) {
            containerHost.getTeamMatchesFromRemoteToLocal()

            expectState { copy(isMatchesLoading = false) }
            expectSideEffect(TeamInfoSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun clickMatch_loadsHead2headForThatMatch() = runTest {
        val standingsRepository = FakeStandingsRepository(
            head2headById = mapOf(COMPLETED_MATCH_ID to match100Head2head)
        )

        viewModel(standingsRepository = standingsRepository).test(
            this,
            TeamInfoState(teamId = "57", isInfoLoading = false, isMatchesLoading = false)
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
            TeamInfoState(
                teamId = "57",
                expandedItem = COMPLETED_MATCH_ID,
                head2head = match100Head2head,
                isInfoLoading = false,
                isMatchesLoading = false
            )
        ) {
            containerHost.matchItemClick(COMPLETED_MATCH_ID)

            expectState { copy(expandedItem = -1) }
        }
    }

    @Test
    fun clickAnotherMatch_loadsThatMatchHead2headNotPrevious() = runTest {
        val standingsRepository = FakeStandingsRepository(
            head2headById = mapOf(
                COMPLETED_MATCH_ID to match100Head2head,
                OTHER_MATCH_ID to match200Head2head
            )
        )

        viewModel(standingsRepository = standingsRepository).test(
            this,
            TeamInfoState(
                teamId = "57",
                expandedItem = COMPLETED_MATCH_ID,
                head2head = match100Head2head,
                isInfoLoading = false,
                isMatchesLoading = false
            )
        ) {
            containerHost.matchItemClick(OTHER_MATCH_ID)

            expectState { copy(expandedItem = OTHER_MATCH_ID, isHead2headLoading = true) }
            expectState { copy(head2head = match200Head2head, isHead2headLoading = false) }
        }
    }

    @Test
    fun clickMatch_whenHead2headFails_hidesSpinner_showsError() = runTest {
        val standingsRepository = FakeStandingsRepository(
            error = ErrorsTypesHttp.Https400Errors(errorCode = 429)
        )

        viewModel(standingsRepository = standingsRepository).test(
            this,
            TeamInfoState(teamId = "57", isInfoLoading = false, isMatchesLoading = false)
        ) {
            containerHost.matchItemClick(COMPLETED_MATCH_ID)

            expectState { copy(expandedItem = COMPLETED_MATCH_ID, isHead2headLoading = true) }
            expectState { copy(isHead2headLoading = false) }
            expectSideEffect(TeamInfoSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    // region Helpers

    private fun viewModel(
        teamRepository: FakeTeamInfoRepository = FakeTeamInfoRepository(),
        newsRepository: FakeNewsRepository = FakeNewsRepository(),
        standingsRepository: FakeStandingsRepository = FakeStandingsRepository()
    ) = TeamInfoViewModel(
        teamRepository = teamRepository,
        stringResourceProvider = FakeStringResourceProvider(),
        standingsRepository = standingsRepository,
        newsRepository = newsRepository,
        teamId = "57"
    )

    // endregion
}

// region Fakes

private class FakeTeamInfoRepository(
    private val teamById: Map<String, TeamInfo> = emptyMap(),
    private val matchesById: Map<String, TeamMatches> = emptyMap(),
    private val error: ErrorsTypesHttp? = null,
    private val matchesError: ErrorsTypesHttp? = null
) : TeamInfoRepository {

    override suspend fun getTeamInfoById(teamId: String): Resource<Boolean> {
        if (error != null) {
            return Resource.Error(httpErrors = error)
        }
        return Resource.Success(true)
    }

    override suspend fun getTeamInfoByIdFlowFromLocal(teamId: String): Flow<TeamInfo?> {
        return flowOf(teamById[teamId])
    }

    override suspend fun getTeamMatchesFromRemoteToLocal(teamId: String): Resource<Boolean> {
        if (matchesError != null) {
            return Resource.Error(httpErrors = matchesError)
        }
        return Resource.Success(true)
    }

    override suspend fun getTeamMatchesFlowFromLocal(teamId: String): Flow<TeamMatches?> {
        return flowOf(matchesById[teamId])
    }
}

private class FakeNewsRepository(
    private val newsByQuery: Map<String, News> = emptyMap()
) : NewsRepository {

    override suspend fun getNews(q: String): Resource<News> {
        val news = newsByQuery[q]
        return if (news != null) {
            Resource.Success(news)
        } else {
            Resource.Success(News())
        }
    }
}

private class FakeStandingsRepository(
    private val head2headById: Map<Int, Head2head> = emptyMap(),
    private val error: ErrorsTypesHttp? = null
) : CompetitionStandingsRepository {

    override suspend fun getHead2headById(matchId: Int): Resource<Head2head> {
        if (error != null) {
            return Resource.Error(httpErrors = error)
        }
        val head2head = head2headById[matchId]
        return if (head2head != null) {
            Resource.Success(head2head)
        } else {
            Resource.Success(Head2head())
        }
    }

    override suspend fun getCompetitionStandingsFromRemoteToLocalById(compId: String) =
        error("not used")

    override suspend fun getStandingsFlowFromLocalById(compId: String) = error("not used")

    override suspend fun getCompetitionTopScorersBySeason(compId: String) = error("not used")

    override suspend fun getScorersFlowFromLocal(compId: String) = error("not used")

    override suspend fun getAllMatchesFromRemoteToLocal(compId: String) = error("not used")

    override suspend fun getAllMatchesFlowFromLocal(compId: String) = error("not used")
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
private const val ARSENAL_NEWS_TITLE = "Arteta praises Saka"

private val arsenal = TeamInfo(
    id = "57",
    name = "Arsenal FC"
)

private val arsenalMatches = TeamMatches(
    id = "57",
    matchesCompleted = listOf(Match(id = COMPLETED_MATCH_ID))
)

private val arsenalNews = News(
    articles = listOf(Article(title = ARSENAL_NEWS_TITLE))
)

private val match100Head2head = Head2head(id = COMPLETED_MATCH_ID)
private val match200Head2head = Head2head(id = OTHER_MATCH_ID)

// endregion
