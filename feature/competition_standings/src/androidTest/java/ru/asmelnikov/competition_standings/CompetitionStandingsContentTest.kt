package ru.asmelnikov.competition_standings

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.asmelnikov.domain.models.Aggregates
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchTeam
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Player
import ru.asmelnikov.domain.models.Score
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.Standing
import ru.asmelnikov.domain.models.Table
import ru.asmelnikov.domain.models.Team
import ru.asmelnikov.domain.models.Time
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@RunWith(AndroidJUnit4::class)
class CompetitionStandingsContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptyStandingsNotLoading_showsReload_hidesTeam() {
        setScreen(competitionStandings = premierLeagueWithoutTable)

        composeTestRule.onNodeWithText(reloadLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("Arsenal").assertDoesNotExist()
    }

    @Test
    fun standingsReload_requestsStandingsUpdate() {
        var standingsReloads = 0
        var scorersReloads = 0
        var matchesReloads = 0

        setScreen(
            competitionStandings = premierLeagueWithoutTable,
            onReloadStandings = { standingsReloads++ },
            onReloadScorers = { scorersReloads++ },
            onReloadMatches = { matchesReloads++ }
        )

        composeTestRule.onNodeWithText(reloadLabel).performClick()

        assertEquals(1, standingsReloads)
        assertEquals(0, scorersReloads)
        assertEquals(0, matchesReloads)
    }

    @Test
    fun loadingEmptyStandings_doesNotShowReload() {
        setScreen(
            competitionStandings = premierLeagueWithoutTable,
            isLoadingStandings = true
        )

        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("Arsenal").assertDoesNotExist()
    }

    @Test
    fun loadingWithStandings_keepsTeamVisible() {
        setScreen(isLoadingStandings = true)

        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
    }

    @Test
    fun pullToRefresh_requestsStandingsUpdate() {
        var standingsReloads = 0
        var scorersReloads = 0
        var matchesReloads = 0

        setScreen(
            onReloadStandings = { standingsReloads++ },
            onReloadScorers = { scorersReloads++ },
            onReloadMatches = { matchesReloads++ }
        )

        swipeDisplayedList()

        assertEquals(1, standingsReloads)
        assertEquals(0, scorersReloads)
        assertEquals(0, matchesReloads)
    }

    @Test
    fun pullToRefresh_requestsScorersUpdate() {
        var standingsReloads = 0
        var scorersReloads = 0
        var matchesReloads = 0

        setScreen(
            matchesCompleted = listOf(completedTour),
            onReloadStandings = { standingsReloads++ },
            onReloadScorers = { scorersReloads++ },
            onReloadMatches = { matchesReloads++ }
        )

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()
        swipeDisplayedList()

        assertEquals(0, standingsReloads)
        assertEquals(1, scorersReloads)
        assertEquals(0, matchesReloads)
    }

    @Test
    fun pullToRefresh_requestsMatchesUpdate() {
        var standingsReloads = 0
        var scorersReloads = 0
        var matchesReloads = 0

        setScreen(
            matchesCompleted = listOf(completedTour),
            onReloadStandings = { standingsReloads++ },
            onReloadScorers = { scorersReloads++ },
            onReloadMatches = { matchesReloads++ }
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        swipeDisplayedList()

        assertEquals(0, standingsReloads)
        assertEquals(0, scorersReloads)
        assertEquals(1, matchesReloads)
    }

    @Test
    fun isLoadingStandings_showsProgressOnStandings_notOnScorersOrMatches() {
        setScreen(
            isLoadingStandings = true,
            matchesCompleted = listOf(completedTour)
        )

        assertEquals(1, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun isLoadingScorers_showsProgressOnScorers_notOnStandingsOrMatches() {
        setScreen(
            isLoadingScorers = true,
            matchesCompleted = listOf(completedTour)
        )

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()

        assertEquals(1, displayedProgressCount())
        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun isLoadingMatches_showsProgressOnMatches_notOnStandingsOrScorers() {
        setScreen(
            isLoadingMatches = true,
            matchesCompleted = listOf(completedTour)
        )

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        assertEquals(1, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun loadedStandings_showsCompetitionAndTeam() {
        setScreen()

        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("Arsenal").assertIsDisplayed()
    }

    @Test
    fun clickTeam_sendsTeamId() {
        var clickedTeamId: Int? = null

        setScreen(onTeamClick = { clickedTeamId = it })

        composeTestRule.onNodeWithText("Arsenal").performClick()

        assertEquals(ARSENAL_ID, clickedTeamId)
    }

    @Test
    fun clickBack_requestsBack() {
        var backCalls = 0

        setScreen(onBackClick = { backCalls++ })

        composeTestRule.onNodeWithContentDescription(backLabel).performClick()

        assertEquals(1, backCalls)
    }

    @Test
    fun emptyScorers_showsReload_hidesPlayer() {
        setScreen(
            scorers = emptyList(),
            matchesCompleted = listOf(completedTour)
        )

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()

        composeTestRule.onNodeWithText(reloadLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("Bukayo Saka").assertDoesNotExist()
    }

    @Test
    fun scorersReload_requestsScorersUpdate() {
        var standingsReloads = 0
        var scorersReloads = 0
        var matchesReloads = 0

        setScreen(
            scorers = emptyList(),
            matchesCompleted = listOf(completedTour),
            onReloadStandings = { standingsReloads++ },
            onReloadScorers = { scorersReloads++ },
            onReloadMatches = { matchesReloads++ }
        )

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()
        composeTestRule.onNodeWithText(reloadLabel).performClick()

        assertEquals(0, standingsReloads)
        assertEquals(1, scorersReloads)
        assertEquals(0, matchesReloads)
    }

    @Test
    fun loadingEmptyScorers_doesNotShowReload() {
        setScreen(
            scorers = emptyList(),
            isLoadingScorers = true,
            matchesCompleted = listOf(completedTour)
        )

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()

        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("Bukayo Saka").assertDoesNotExist()
    }

    @Test
    fun clickPlayer_sendsPersonId() {
        var clickedPersonId: Int? = null

        setScreen(
            matchesCompleted = listOf(completedTour),
            onPersonClick = { clickedPersonId = it }
        )

        composeTestRule.onNodeWithText(scorersTabLabel).performClick()
        composeTestRule.onNodeWithText("Bukayo Saka").performClick()

        assertEquals(SAKA_ID, clickedPersonId)
    }

    @Test
    fun emptyMatches_showsReload_hidesMatches() {
        setScreen()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText(reloadLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertDoesNotExist()
    }

    @Test
    fun matchesReload_requestsMatchesUpdate() {
        var standingsReloads = 0
        var scorersReloads = 0
        var matchesReloads = 0

        setScreen(
            onReloadStandings = { standingsReloads++ },
            onReloadScorers = { scorersReloads++ },
            onReloadMatches = { matchesReloads++ }
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(reloadLabel).performClick()

        assertEquals(0, standingsReloads)
        assertEquals(0, scorersReloads)
        assertEquals(1, matchesReloads)
    }

    @Test
    fun loadingEmptyMatches_doesNotShowReload() {
        setScreen(isLoadingMatches = true)

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertDoesNotExist()
    }

    @Test
    fun clickMatchesTab_showsCompletedMatch() {
        setScreen(matchesCompleted = listOf(completedTour))

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun clickAheadTab_showsUpcomingMatch() {
        setScreen(
            matchesCompleted = listOf(completedTour),
            matchesAhead = listOf(aheadTour)
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(aheadTabLabel).performClick()

        composeTestRule.onNodeWithText("Spurs - Arsenal").assertIsDisplayed()
    }

    @Test
    fun clickMatch_sendsMatchId() {
        var clickedMatchId: Int? = null

        setScreen(
            matchesCompleted = listOf(completedTour),
            onMatchItemClick = { clickedMatchId = it }
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText("Arsenal - Chelsea").performClick()

        assertEquals(COMPLETED_MATCH_ID, clickedMatchId)
    }

    @Test
    fun expandedMatch_showsHead2head() {
        setScreen(
            matchesCompleted = listOf(completedTour),
            expandedItemId = COMPLETED_MATCH_ID,
            head2head = Head2head(
                id = COMPLETED_MATCH_ID,
                aggregates = Aggregates(numberOfMatches = 0)
            )
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText(firstMatchLabel).assertIsDisplayed()
    }

    // region Helpers

    private val reloadLabel: String
        get() = composeTestRule.activity.getString(R.string.reload)

    private val backLabel: String
        get() = composeTestRule.activity.getString(R.string.back)

    private val scorersTabLabel: String
        get() = composeTestRule.activity.getString(R.string.tab_scorers)

    private val matchesTabLabel: String
        get() = composeTestRule.activity.getString(R.string.tab_matches)

    private val aheadTabLabel: String
        get() = composeTestRule.activity.getString(R.string.tab_matches_ahead)

    private val firstMatchLabel: String
        get() = composeTestRule.activity.getString(R.string.first_match_no_statistics)

    private fun displayedProgressCount(): Int {
        val indicators = composeTestRule
            .onAllNodes(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
        return indicators.fetchSemanticsNodes().indices.count { index ->
            indicators[index].isDisplayed()
        }
    }

    private fun swipeDisplayedList() {
        val lists = composeTestRule.onAllNodes(hasScrollToIndexAction())
        val index = lists.fetchSemanticsNodes().indices.first { lists[it].isDisplayed() }
        lists[index].performTouchInput { swipeDown() }
    }

    private fun setScreen(
        competitionStandings: CompetitionStandings = premierLeague,
        scorers: List<Scorer> = listOf(sakaScorer),
        matchesCompleted: List<MatchesByTour> = emptyList(),
        matchesAhead: List<MatchesByTour> = emptyList(),
        isLoadingStandings: Boolean = false,
        isLoadingScorers: Boolean = false,
        isLoadingMatches: Boolean = false,
        expandedItemId: Int = -1,
        head2head: Head2head = Head2head(),
        isHead2headLoading: Boolean = false,
        onReloadStandings: () -> Unit = {},
        onReloadScorers: () -> Unit = {},
        onReloadMatches: () -> Unit = {},
        onTeamClick: (Int) -> Unit = {},
        onPersonClick: (Int) -> Unit = {},
        onMatchItemClick: (Int) -> Unit = {},
        onBackClick: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                GoalPulseTheme {
                    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = true) {
                            CompetitionStandingsContent(
                                compUrl = "",
                                competitionStandings = competitionStandings,
                                onBackClick = onBackClick,
                                isLoadingStandings = isLoadingStandings,
                                scorers = scorers,
                                isLoadingScorers = isLoadingScorers,
                                matchesCompleted = matchesCompleted,
                                matchesAhead = matchesAhead,
                                isLoadingMatches = isLoadingMatches,
                                expandedItemId = expandedItemId,
                                onMatchItemClick = onMatchItemClick,
                                head2head = head2head,
                                isHead2headLoading = isHead2headLoading,
                                onTeamClick = onTeamClick,
                                onReloadStandingsClick = onReloadStandings,
                                onReloadScorersClick = onReloadScorers,
                                onReloadMatchesClick = onReloadMatches,
                                onPersonClick = onPersonClick,
                                animatedVisibilityScope = this
                            )
                        }
                    }
                }
            }
        }
    }

    // endregion
}

// region Test data

private const val ARSENAL_ID = 57
private const val SAKA_ID = 44
private const val COMPLETED_MATCH_ID = 100
private const val AHEAD_MATCH_ID = 200

private val premierLeagueWithoutTable = CompetitionStandings(
    id = "2021",
    competition = Competition(id = 2021, name = "Premier League"),
    standings = emptyList()
)

private val premierLeague = CompetitionStandings(
    id = "2021",
    competition = Competition(id = 2021, name = "Premier League"),
    standings = listOf(
        Standing(
            table = listOf(
                Table(
                    position = 1,
                    team = Team(
                        id = ARSENAL_ID,
                        name = "Arsenal FC",
                        shortName = "Arsenal"
                    )
                )
            )
        )
    )
)

private val sakaScorer = Scorer(
    goals = 15,
    player = Player(id = SAKA_ID, name = "Bukayo Saka"),
    team = Team(id = ARSENAL_ID, shortName = "AFC")
)

private val completedTour = MatchesByTour(
    matchDay = 1,
    matches = listOf(
        Match(
            id = COMPLETED_MATCH_ID,
            homeTeam = MatchTeam(id = ARSENAL_ID, shortName = "Arsenal"),
            awayTeam = MatchTeam(id = 61, shortName = "Chelsea"),
            score = Score(fullTime = Time(home = 2, away = 1)),
            utcDate = "2026-05-09T14:00:00Z"
        )
    )
)

private val aheadTour = MatchesByTour(
    matchDay = 2,
    matches = listOf(
        Match(
            id = AHEAD_MATCH_ID,
            homeTeam = MatchTeam(id = 73, shortName = "Spurs"),
            awayTeam = MatchTeam(id = ARSENAL_ID, shortName = "Arsenal"),
            bigDate = "12.05",
            utcDate = "2026-05-12T14:00:00Z"
        )
    )
)

// endregion
