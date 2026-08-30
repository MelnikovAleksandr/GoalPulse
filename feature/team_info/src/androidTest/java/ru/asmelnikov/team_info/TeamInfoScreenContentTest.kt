package ru.asmelnikov.team_info

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.asmelnikov.domain.models.Aggregates
import ru.asmelnikov.domain.models.Article
import ru.asmelnikov.domain.models.Coach
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchTeam
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.PlayerPosition
import ru.asmelnikov.domain.models.Score
import ru.asmelnikov.domain.models.Squad
import ru.asmelnikov.domain.models.SquadByPosition
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.Time
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@RunWith(AndroidJUnit4::class)
class TeamInfoScreenContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptySquadNotLoading_showsReload_hidesPlayers() {
        setScreen(teamInfo = arsenalWithoutSquad)

        composeTestRule.onNodeWithText(reloadLabel).assertExists()
        composeTestRule.onNodeWithText("Bukayo Saka").assertDoesNotExist()
        composeTestRule.onNodeWithText("Mikel Arteta").assertDoesNotExist()
    }

    @Test
    fun squadReload_requestsTeamInfoUpdate() {
        var teamInfoReloads = 0
        var matchesReloads = 0

        setScreen(
            teamInfo = arsenalWithoutSquad,
            onTeamInfoReload = { teamInfoReloads++ },
            onMatchesReload = { matchesReloads++ },
        )

        composeTestRule.onNodeWithText(reloadLabel).performScrollTo().performClick()

        assertEquals(1, teamInfoReloads)
        assertEquals(0, matchesReloads)
    }

    @Test
    fun loadingEmptySquad_doesNotShowReload() {
        setScreen(teamInfo = arsenalWithoutSquad, isLoading = true)

        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("Bukayo Saka").assertDoesNotExist()
    }

    @Test
    fun loadingWithSquad_keepsPlayersVisible() {
        setScreen(isLoading = true)

        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mikel Arteta").assertIsDisplayed()
        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
    }

    @Test
    fun pullToRefresh_requestsTeamInfoUpdate() {
        var teamInfoReloads = 0
        var matchesReloads = 0

        setScreen(
            onTeamInfoReload = { teamInfoReloads++ },
            onMatchesReload = { matchesReloads++ },
        )

        swipeDisplayedList()

        assertEquals(1, teamInfoReloads)
        assertEquals(0, matchesReloads)
    }

    @Test
    fun pullToRefresh_requestsMatchesUpdate() {
        var teamInfoReloads = 0
        var matchesReloads = 0

        setScreen(
            matchesComplete = listOf(completedMatch),
            onTeamInfoReload = { teamInfoReloads++ },
            onMatchesReload = { matchesReloads++ },
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        swipeDisplayedList()

        assertEquals(0, teamInfoReloads)
        assertEquals(1, matchesReloads)
    }

    @Test
    fun isLoading_showsProgressOnSquadAndInfo_notOnMatches() {
        setScreen(
            isLoading = true,
            matchesComplete = listOf(completedMatch),
        )

        assertEquals(1, displayedProgressCount())
        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()

        composeTestRule.onNodeWithText(infoTabLabel).performClick()

        assertEquals(1, displayedProgressCount())
        composeTestRule.onNodeWithText("Holloway, London").assertIsDisplayed()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun isMatchesLoading_showsProgressOnMatches_notOnSquadOrInfo() {
        setScreen(
            isMatchesLoading = true,
            matchesComplete = listOf(completedMatch),
        )

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()

        composeTestRule.onNodeWithText(infoTabLabel).performClick()

        assertEquals(0, displayedProgressCount())
        composeTestRule.onNodeWithText("Holloway, London").assertIsDisplayed()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        assertEquals(1, displayedProgressCount())
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun loadedSquad_showsTeamCoachAndPlayer() {
        setScreen()

        composeTestRule.onNodeWithText("Arsenal FC").assertIsDisplayed()
        composeTestRule.onNodeWithText(managerLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("Mikel Arteta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bukayo Saka").assertIsDisplayed()
        composeTestRule.onNodeWithText(rightWingerLabel).assertIsDisplayed()
    }

    @Test
    fun clickPlayer_sendsPersonId() {
        var clickedPersonId: Int? = null

        setScreen(onPersonClick = { clickedPersonId = it })

        composeTestRule.onNodeWithText("Bukayo Saka").performClick()

        assertEquals(SAKA_ID, clickedPersonId)
    }

    @Test
    fun clickBack_requestsBack() {
        var backCalls = 0

        setScreen(onBackClick = { backCalls++ })

        composeTestRule.onNodeWithContentDescription(backLabel).performClick()

        assertEquals(1, backCalls)
    }

    @Test
    fun clickInfoTab_showsAddressVenueAndNews() {
        setScreen(news = arsenalNews)

        composeTestRule.onNodeWithText(infoTabLabel).performClick()

        composeTestRule.onNodeWithText("Holloway, London").assertIsDisplayed()
        composeTestRule.onNodeWithText("Emirates Stadium").assertIsDisplayed()
        composeTestRule.onNodeWithText(newsLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(ARSENAL_NEWS_TITLE).assertIsDisplayed()
    }

    @Test
    fun emptyMatches_showsReload_hidesMatches() {
        setScreen()

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText(reloadLabel).assertExists()
        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertDoesNotExist()
    }

    @Test
    fun matchesReload_requestsMatchesUpdate() {
        var teamInfoReloads = 0
        var matchesReloads = 0

        setScreen(
            onTeamInfoReload = { teamInfoReloads++ },
            onMatchesReload = { matchesReloads++ },
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(reloadLabel).performScrollTo().performClick()

        assertEquals(0, teamInfoReloads)
        assertEquals(1, matchesReloads)
    }

    @Test
    fun clickMatchesTab_showsCompletedMatch() {
        setScreen(matchesComplete = listOf(completedMatch))

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText("Arsenal - Chelsea").assertIsDisplayed()
    }

    @Test
    fun clickAheadTab_showsUpcomingMatch() {
        setScreen(
            matchesComplete = listOf(completedMatch),
            matchesAhead = listOf(aheadMatch),
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(aheadTabLabel).performClick()

        composeTestRule.onNodeWithText("Spurs - Arsenal").assertIsDisplayed()
    }

    @Test
    fun clickMatch_sendsMatchId() {
        var clickedMatchId: Int? = null

        setScreen(
            matchesComplete = listOf(completedMatch),
            onMatchItemClick = { clickedMatchId = it },
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText("Arsenal - Chelsea").performClick()

        assertEquals(COMPLETED_MATCH_ID, clickedMatchId)
    }

    @Test
    fun expandedMatch_showsHead2head() {
        setScreen(
            matchesComplete = listOf(completedMatch),
            expandedItemId = COMPLETED_MATCH_ID,
            head2head = Head2head(
                id = COMPLETED_MATCH_ID,
                aggregates = Aggregates(numberOfMatches = 0),
            ),
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithText(firstMatchLabel).assertIsDisplayed()
    }

    @Test
    fun completedMatch_doesNotShowCalendarButton() {
        setScreen(matchesComplete = listOf(completedMatch))

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()

        composeTestRule.onNodeWithContentDescription(calendarAddLabel).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(calendarRemoveLabel).assertDoesNotExist()
    }

    @Test
    fun aheadMatch_showsAddCalendarButton() {
        setScreen(
            matchesComplete = listOf(completedMatch),
            matchesAhead = listOf(aheadMatch),
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(aheadTabLabel).performClick()

        composeTestRule.onNodeWithContentDescription(calendarAddLabel).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(calendarRemoveLabel).assertDoesNotExist()
    }

    @Test
    fun scheduledAheadMatch_showsRemoveCalendarButton() {
        setScreen(
            matchesComplete = listOf(completedMatch),
            matchesAhead = listOf(aheadMatch),
            calendarMatchIds = setOf(AHEAD_MATCH_ID),
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(aheadTabLabel).performClick()

        composeTestRule.onNodeWithContentDescription(calendarRemoveLabel).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(calendarAddLabel).assertDoesNotExist()
    }

    @Test
    fun clickCalendar_sendsMatch_doesNotExpandMatch() {
        var clickedMatchId: Int? = null
        var calendarMatch: Match? = null

        setScreen(
            matchesComplete = listOf(completedMatch),
            matchesAhead = listOf(aheadMatch),
            onMatchItemClick = { clickedMatchId = it },
            onCalendarClick = { calendarMatch = it },
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(aheadTabLabel).performClick()
        composeTestRule.onNodeWithContentDescription(calendarAddLabel).performClick()

        assertEquals(AHEAD_MATCH_ID, calendarMatch?.id)
        assertEquals(null, clickedMatchId)
    }

    @Test
    fun calendarLoading_hidesIcon_showsProgress() {
        setScreen(
            matchesComplete = listOf(completedMatch),
            matchesAhead = listOf(aheadMatch),
            calendarBusyMatchIds = setOf(AHEAD_MATCH_ID),
        )

        composeTestRule.onNodeWithText(matchesTabLabel).performClick()
        composeTestRule.onNodeWithText(aheadTabLabel).performClick()

        composeTestRule.onNodeWithContentDescription(calendarAddLabel).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(calendarRemoveLabel).assertDoesNotExist()
        assertEquals(1, displayedProgressCount())
    }

    // region Helpers

    private val reloadLabel: String
        get() = composeTestRule.activity.getString(R.string.reload)

    private val backLabel: String
        get() = composeTestRule.activity.getString(R.string.back)

    private val managerLabel: String
        get() = composeTestRule.activity.getString(R.string.team_manager)

    private val rightWingerLabel: String
        get() = composeTestRule.activity.getString(R.string.position_right_winger)

    private val infoTabLabel: String
        get() = composeTestRule.activity.getString(R.string.team_tab_info)

    private val matchesTabLabel: String
        get() = composeTestRule.activity.getString(R.string.team_tab_matches)

    private val aheadTabLabel: String
        get() = composeTestRule.activity.getString(R.string.tab_matches_ahead)

    private val newsLabel: String
        get() = composeTestRule.activity.getString(R.string.team_news)

    private val firstMatchLabel: String
        get() = composeTestRule.activity.getString(R.string.first_match_no_statistics)

    private val calendarAddLabel: String
        get() = composeTestRule.activity.getString(R.string.calendar_add_event)

    private val calendarRemoveLabel: String
        get() = composeTestRule.activity.getString(R.string.calendar_remove_event)

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
        teamInfo: TeamInfo = arsenal,
        isLoading: Boolean = false,
        isMatchesLoading: Boolean = false,
        matchesComplete: List<Match> = emptyList(),
        matchesAhead: List<Match> = emptyList(),
        expandedItemId: Int = -1,
        head2head: Head2head = Head2head(),
        isHead2headLoading: Boolean = false,
        news: News = News(),
        onTeamInfoReload: () -> Unit = {},
        onMatchesReload: () -> Unit = {},
        onPersonClick: (Int) -> Unit = {},
        onMatchItemClick: (Int) -> Unit = {},
        onBackClick: () -> Unit = {},
        calendarMatchIds: Set<Int> = emptySet(),
        calendarBusyMatchIds: Set<Int> = emptySet(),
        onCalendarClick: (Match) -> Unit = {},
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                GoalPulseTheme {
                    TeamInfoScreenContent(
                        teamInfo = teamInfo,
                        isLoading = isLoading,
                        onBackClick = onBackClick,
                        onTeamInfoReload = onTeamInfoReload,
                        isMatchesLoading = isMatchesLoading,
                        matchesComplete = matchesComplete,
                        matchesAhead = matchesAhead,
                        teamId = ARSENAL_ID,
                        expandedItemId = expandedItemId,
                        onMatchItemClick = onMatchItemClick,
                        head2head = head2head,
                        isHead2headLoading = isHead2headLoading,
                        onMatchesReload = onMatchesReload,
                        onPersonClick = onPersonClick,
                        news = news,
                        isLoadingNews = false,
                        calendarMatchIds = calendarMatchIds,
                        calendarBusyMatchIds = calendarBusyMatchIds,
                        onCalendarClick = onCalendarClick,
                    )
                }
            }
        }
    }

    // endregion
}

// region Test data

private const val ARSENAL_ID = "57"
private const val SAKA_ID = 44
private const val COMPLETED_MATCH_ID = 100
private const val AHEAD_MATCH_ID = 200
private const val ARSENAL_NEWS_TITLE = "Arteta praises Saka"

private val arsenalWithoutSquad = TeamInfo(
    id = ARSENAL_ID,
    name = "Arsenal FC",
)

private val arsenal = TeamInfo(
    id = ARSENAL_ID,
    name = "Arsenal FC",
    address = "Holloway, London",
    venue = "Emirates Stadium",
    founded = 1886,
    coach = Coach(id = 1, name = "Mikel Arteta"),
    squadByPosition = listOf(
        SquadByPosition(
            position = PlayerPosition.RIGHT_WINGER,
            squad = listOf(
                Squad(
                    id = SAKA_ID,
                    name = "Bukayo Saka",
                    nationality = "England",
                    age = "22",
                ),
            ),
        ),
    ),
)

private val arsenalNews = News(
    articles = listOf(Article(title = ARSENAL_NEWS_TITLE)),
)

private val completedMatch = Match(
    id = COMPLETED_MATCH_ID,
    homeTeam = MatchTeam(id = 57, shortName = "Arsenal"),
    awayTeam = MatchTeam(id = 61, shortName = "Chelsea"),
    score = Score(fullTime = Time(home = 2, away = 1)),
    utcDate = "2026-05-09T14:00:00Z",
)

private val aheadMatch = Match(
    id = AHEAD_MATCH_ID,
    homeTeam = MatchTeam(id = 73, shortName = "Spurs"),
    awayTeam = MatchTeam(id = 57, shortName = "Arsenal"),
    bigDate = "12.05",
    utcDate = "2026-05-12T14:00:00Z",
)

// endregion
