package ru.asmelnikov.competition_standings

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialContainerTransformSpec
import com.mxalbert.sharedelements.SharedMaterialContainer
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competition_standings.components.FirstPagerScreenStandings
import ru.asmelnikov.competition_standings.components.SecondPagerScreenScorers
import ru.asmelnikov.competition_standings.components.ThirdPagerScreenMatches
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingSideEffects
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.PagerTabRow
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.navigateWithArgs
import ru.asmelnikov.utils.navigation.popUp
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun CompetitionStandingsScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: CompetitionStandingsViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CompetitionStandingSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

            is CompetitionStandingSideEffects.BackClick -> appState.popUp()

            is CompetitionStandingSideEffects.OnTeamInfoNavigate -> {
                appState.navigateWithArgs(route = Routes.Team_Info, args = it.teamId)
            }
            is CompetitionStandingSideEffects.OnPersonInfoNavigate -> {
                appState.navigateWithArgs(route = Routes.Person_Info, args = it.personId)
            }
        }
    }

    CompetitionStandingsContent(
        competitionStandings = state.competitionStandings,
        seasons = state.seasons.map { it.startDateEndDate },
        currentSeasonStandings = state.currentSeasonStandings,
        onSeasonStandingsUpdate = viewModel::updateStandingsFromRemoteToLocal,
        onBackClick = viewModel::onBackClick,
        isLoadingStandings = state.isLoadingStandings,
        currentSeasonScorers = state.currentSeasonScorers,
        onSeasonScorersUpdate = viewModel::updateScorersFromRemoteToLocal,
        isLoadingScorers = state.isLoadingScorers,
        scorers = state.scorers,
        matchesCompleted = state.matchesCompleted,
        matchesAhead = state.matchesAhead,
        currentSeasonMatches = state.currentSeasonMatches,
        onSeasonMatchesUpdate = viewModel::updateMatchesFromRemoteToLocal,
        isLoadingMatches = state.isLoadingMatches,
        expandedItemId = state.expandedItem,
        onMatchItemClick = viewModel::matchItemClick,
        head2head = state.head2head,
        isHead2headLoading = state.isHead2headLoading,
        onTeamClick = viewModel::onTeamClick,
        onReloadStandingsClick = viewModel::updateStandingsFromRemoteToLocal,
        onReloadMatchesClick = viewModel::updateMatchesFromRemoteToLocal,
        onReloadScorersClick = viewModel::updateScorersFromRemoteToLocal,
        onPersonClick = viewModel::onPersonClick
    )
}

@OptIn(ExperimentalToolbarApi::class, ExperimentalFoundationApi::class)
@Composable
fun CompetitionStandingsContent(
    competitionStandings: CompetitionStandings?,
    seasons: List<String>,
    onBackClick: () -> Unit,
    currentSeasonStandings: String,
    onSeasonStandingsUpdate: (String) -> Unit,
    isLoadingStandings: Boolean,
    scorers: List<Scorer>,
    currentSeasonScorers: String,
    onSeasonScorersUpdate: (String) -> Unit,
    isLoadingScorers: Boolean,
    matchesCompleted: List<MatchesByTour>,
    matchesAhead: List<MatchesByTour>,
    currentSeasonMatches: String,
    onSeasonMatchesUpdate: (String) -> Unit,
    isLoadingMatches: Boolean,
    expandedItemId: Int,
    onMatchItemClick: (Int) -> Unit,
    head2head: Head2head = Head2head(),
    isHead2headLoading: Boolean = false,
    onTeamClick: (Int) -> Unit,
    onReloadStandingsClick: () -> Unit,
    onReloadScorersClick: () -> Unit,
    onReloadMatchesClick: () -> Unit,
    onPersonClick: (Int) -> Unit
) {

    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        3
    }
    val orientation =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) "LANDSCAPE" else "PORTRAIT"

    Box {

        val state = rememberCollapsingToolbarScaffoldState()

        LaunchedEffect(key1 = orientation) {
            if (orientation == "LANDSCAPE")
                state.toolbarState.collapse()
        }

        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = state,
            enabled = orientation == "PORTRAIT",
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                val progress = state.toolbarState.progress
                val textSize = (18 + (18 * progress)).sp

                SubComposeAsyncImageCommon(
                    imageUri = competitionStandings?.area?.flag ?: "",
                    shape = RoundedCornerShape(0.dp),
                    size = MaterialTheme.dimens.emptyContentImageSize,
                    alpha = state.toolbarState.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax()
                        .pin(),
                    loading = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(MaterialTheme.dimens.large)
                            )
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = MaterialTheme.dimens.small1,
                            vertical = MaterialTheme.dimens.medium2
                        )
                        .road(
                            whenCollapsed = Alignment.TopEnd,
                            whenExpanded = Alignment.Center
                        )
                ) {

                    val imgSize = (40 + (100 * progress)).dp

                    SharedMaterialContainer(
                        key = competitionStandings?.competition?.emblem ?: "",
                        screenKey = Routes.Competition_Standings,
                        color = Color.Transparent,
                        transitionSpec = MaterialContainerTransformSpec(
                            durationMillis = 1000,
                            fadeMode = FadeMode.Out
                        )
                    ) {
                        SubComposeAsyncImageCommon(
                            imageUri = competitionStandings?.competition?.emblem ?: "",
                            shape = RoundedCornerShape(0.dp),
                            size = imgSize
                        )
                    }
                }

                Text(
                    text = competitionStandings?.competition?.name ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = textSize,
                    modifier = Modifier
                        .padding(
                            bottom = MaterialTheme.dimens.medium1,
                            top = MaterialTheme.dimens.medium3
                        )
                        .road(
                            whenCollapsed = Alignment.TopCenter,
                            whenExpanded = Alignment.BottomCenter
                        )
                )

            }) {

            Column(modifier = Modifier.fillMaxSize()) {

                PagerTabRow(
                    tabTitles = listOf("Standings", "Scorers", "Matches"),
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
                    pagerState = pagerState
                )

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = pagerState,
                    beyondViewportPageCount = 2,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (page) {
                        0 -> {
                            FirstPagerScreenStandings(
                                competitionStandings = competitionStandings,
                                seasons = seasons,
                                currentSeason = currentSeasonStandings,
                                onSeasonUpdate = onSeasonStandingsUpdate,
                                isLoading = isLoadingStandings,
                                onTeamClick = onTeamClick,
                                onReloadClick = onReloadStandingsClick
                            )
                        }

                        1 -> {
                            SecondPagerScreenScorers(
                                scorers = scorers,
                                seasons = seasons,
                                currentSeasonScorers = currentSeasonScorers,
                                onSeasonScorersUpdate = onSeasonScorersUpdate,
                                isLoadingScorers = isLoadingScorers,
                                onReloadClick = onReloadScorersClick,
                                onPersonClick = onPersonClick
                            )
                        }

                        2 -> {
                            ThirdPagerScreenMatches(
                                matchesCompleted = matchesCompleted,
                                matchesAhead = matchesAhead,
                                seasons = seasons,
                                currentSeasonMatches = currentSeasonMatches,
                                onSeasonMatchesUpdate = onSeasonMatchesUpdate,
                                isLoadingMatches = isLoadingMatches,
                                expandedItemId = expandedItemId,
                                onMatchItemClick = onMatchItemClick,
                                head2head = head2head,
                                isHead2headLoading = isHead2headLoading,
                                onReloadClick = onReloadMatchesClick
                            )
                        }
                    }
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = MaterialTheme.dimens.medium1), onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
