package ru.asmelnikov.competitions_main

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
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@RunWith(AndroidJUnit4::class)
class CompetitionsScreenContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptyListNotLoading_showsReload_hidesLeagues() {
        setScreen(comps = emptyList())

        composeTestRule.onNodeWithText(reloadLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("Premier League").assertDoesNotExist()
        composeTestRule.onNodeWithText(notFoundLabel).assertDoesNotExist()
    }

    @Test
    fun reload_requestsUpdate() {
        var updateCalls = 0

        setScreen(comps = emptyList(), onUpdate = { updateCalls++ })

        composeTestRule.onNodeWithText(reloadLabel).performClick()

        assertEquals(1, updateCalls)
    }

    @Test
    fun pullToRefresh_requestsUpdate() {
        var updateCalls = 0

        setScreen(comps = listOf(premierLeague, laLiga), onUpdate = { updateCalls++ })

        composeTestRule.onNode(hasScrollToIndexAction()).performTouchInput { swipeDown() }

        assertEquals(1, updateCalls)
    }

    @Test
    fun pullToRefresh_showsProgressWhileLoading() {
        setScreen(comps = listOf(premierLeague, laLiga), isLoading = true)

        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("La Liga").assertIsDisplayed()
    }

    @Test
    fun loadingWithEmptyList_doesNotShowReloadOrLeagues() {
        setScreen(comps = emptyList(), isLoading = true)

        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("Premier League").assertDoesNotExist()
        composeTestRule.onNodeWithText(notFoundLabel).assertDoesNotExist()
    }

    @Test
    fun loadingWithLeaguesOnScreen_keepsLeaguesVisible() {
        setScreen(comps = listOf(premierLeague, laLiga), isLoading = true)

        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("La Liga").assertIsDisplayed()
        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
    }

    @Test
    fun loadedLeagues_showsNamesAndTotalCount() {
        setScreen(comps = listOf(premierLeague, laLiga))

        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("La Liga").assertIsDisplayed()
        composeTestRule.onNodeWithText(twoLeaguesCountLabel).assertIsDisplayed()
    }

    @Test
    fun clickLeague_sendsIdAndEmblem() {
        var clickedId: String? = null
        var clickedUrl: String? = null

        setScreen(
            comps = listOf(premierLeague, laLiga),
            onCompClick = { id, url ->
                clickedId = id
                clickedUrl = url
            }
        )

        composeTestRule.onNodeWithText("Premier League").performClick()

        assertEquals("2021", clickedId)
        assertEquals("https://crests.football-data.org/PL.png", clickedUrl)
    }

    @Test
    fun search_hidesNonMatchingLeagues() {
        setScreen(comps = listOf(premierLeague, laLiga))

        composeTestRule.onNode(hasSetTextAction()).performTextInput("premier")

        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("La Liga").assertDoesNotExist()
        composeTestRule.onNodeWithText(notFoundLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
    }

    @Test
    fun searchWithNoHits_showsNotFound_notReload() {
        setScreen(comps = listOf(premierLeague))

        composeTestRule.onNode(hasSetTextAction()).performTextInput("xyz")

        composeTestRule.onNodeWithText(notFoundLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("Premier League").assertDoesNotExist()
        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
    }

    @Test
    fun clearSearch_showsAllLeaguesAgain() {
        setScreen(comps = listOf(premierLeague, laLiga))

        composeTestRule.onNode(hasSetTextAction()).performTextInput("premier")
        composeTestRule
            .onNodeWithContentDescription(clearSearchLabel, useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("Premier League").assertIsDisplayed()
        composeTestRule.onNodeWithText("La Liga").assertIsDisplayed()
        composeTestRule.onNodeWithText(notFoundLabel).assertDoesNotExist()
    }

    // region Helpers

    private val reloadLabel: String
        get() = composeTestRule.activity.getString(R.string.reload)

    private val notFoundLabel: String
        get() = composeTestRule.activity.getString(R.string.competitions_not_found)

    private val clearSearchLabel: String
        get() = composeTestRule.activity.getString(R.string.clear_search)

    private val twoLeaguesCountLabel: String
        get() = composeTestRule.activity.getString(R.string.available_competitions_count, 2)

    private fun setScreen(
        comps: List<Competition>,
        isLoading: Boolean = false,
        onUpdate: () -> Unit = {},
        onCompClick: (String, String) -> Unit = { _, _ -> }
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                GoalPulseTheme {
                    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = true) {
                            CompetitionsScreenContent(
                                comps = comps,
                                updateComps = onUpdate,
                                isLoading = isLoading,
                                onCompClick = onCompClick,
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
