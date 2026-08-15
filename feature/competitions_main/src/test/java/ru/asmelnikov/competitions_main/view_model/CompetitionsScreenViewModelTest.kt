package ru.asmelnikov.competitions_main.view_model

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.repository.CompetitionsRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider

class CompetitionsScreenViewModelTest {

    @Test
    fun onOpen_showsCachedLeaguesBeforeNetworkFinishesThenStopsLoading() = runTest {
        val network = CompletableDeferred<Unit>()
        val repository = FakeCompetitionsRepository(
            localCompetitions = MutableStateFlow(listOf(premierLeague, laLiga)),
            remoteGate = network,
            remoteResult = Resource.Success(true)
        )

        viewModel(repository).test(this) {
            runOnCreate()

            expectState { copy(comps = listOf(premierLeague, laLiga)) }

            network.complete(Unit)

            expectState { copy(isLoading = false) }

            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun onOpen_whenNetworkFails_keepsCachedLeagues_hidesSpinner_showsError() = runTest {
        val network = CompletableDeferred<Unit>()
        val repository = FakeCompetitionsRepository(
            localCompetitions = MutableStateFlow(listOf(premierLeague)),
            remoteGate = network,
            remoteResult = Resource.Error(
                httpErrors = ErrorsTypesHttp.Https400Errors(errorCode = 429)
            )
        )

        viewModel(repository).test(this) {
            runOnCreate()

            expectState { copy(comps = listOf(premierLeague)) }

            network.complete(Unit)

            expectState { copy(isLoading = false) }
            expectSideEffect(CompetitionsScreenSideEffects.Snackbar(RATE_LIMIT_MESSAGE))

            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun onOpen_whenNetworkFailsAndCacheEmpty_hidesSpinner_showsEmptyAndError() = runTest {
        val repository = FakeCompetitionsRepository(
            remoteResult = Resource.Error(
                httpErrors = ErrorsTypesHttp.Https400Errors(errorCode = 429)
            )
        )

        viewModel(repository).test(this) {
            runOnCreate()

            expectState { copy(isLoading = false) }
            expectSideEffect(CompetitionsScreenSideEffects.Snackbar(RATE_LIMIT_MESSAGE))

            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun cacheEmitsNewLeagues_replacesListOnScreen() = runTest {
        val localCompetitions = MutableStateFlow<List<Competition>>(emptyList())
        val repository = FakeCompetitionsRepository(
            localCompetitions = localCompetitions,
            remoteResult = Resource.Success(true)
        )

        viewModel(repository).test(this) {
            runOnCreate()

            expectState { copy(isLoading = false) }

            localCompetitions.value = listOf(premierLeague, laLiga)

            expectState { copy(comps = listOf(premierLeague, laLiga)) }

            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun pullToRefresh_keepsLeaguesOnScreenWhileLoadingThenStopsSpinner() = runTest {
        val network = CompletableDeferred<Unit>()
        val repository = FakeCompetitionsRepository(
            remoteGate = network,
            remoteResult = Resource.Success(true)
        )

        viewModel(repository).test(
            this,
            CompetitionsScreenState(comps = listOf(premierLeague), isLoading = false)
        ) {
            containerHost.updateCompetitionsFromRemoteToLocal()

            expectState { copy(isLoading = true) }

            network.complete(Unit)

            expectState { copy(isLoading = false) }
        }
    }

    @Test
    fun clickLeague_opensStandingsWithSameIdAndEmblem() = runTest {
        viewModel(FakeCompetitionsRepository()).test(this) {
            containerHost.onCompClick(
                compId = "2021",
                compUrl = "https://crests.football-data.org/PL.png"
            )

            expectSideEffect(
                CompetitionsScreenSideEffects.OnCompetitionNavigate(
                    compId = "2021",
                    compUrl = "https://crests.football-data.org/PL.png"
                )
            )
        }
    }

    // region Helpers

    private fun viewModel(repository: FakeCompetitionsRepository) = CompetitionsScreenViewModel(
        footballRepository = repository,
        stringResourceProvider = FakeStringResourceProvider(),
        savedStateHandle = SavedStateHandle()
    )

    // endregion
}

// region Fakes

private class FakeCompetitionsRepository(
    val localCompetitions: MutableStateFlow<List<Competition>> = MutableStateFlow(emptyList()),
    var remoteResult: Resource<Boolean> = Resource.Success(true),
    private val remoteGate: CompletableDeferred<Unit>? = null
) : CompetitionsRepository {

    override suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<Boolean> {
        remoteGate?.await()
        return remoteResult
    }

    override suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>> {
        return localCompetitions
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

private val premierLeague = Competition(
    id = 2021,
    name = "Premier League",
    emblem = "https://crests.football-data.org/PL.png"
)

private val laLiga = Competition(
    id = 2014,
    name = "La Liga",
    emblem = "https://crests.football-data.org/PD.png"
)

// endregion
