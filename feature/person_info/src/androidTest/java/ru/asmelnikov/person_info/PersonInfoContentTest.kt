package ru.asmelnikov.person_info

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.asmelnikov.domain.models.CurrentTeam
import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.domain.models.PlayerPosition
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme

@RunWith(AndroidJUnit4::class)
class PersonInfoContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptyNotLoading_showsReload_hidesPlayer() {
        setScreen(person = emptyPerson)

        composeTestRule.onNodeWithText(reloadLabel).assertExists()
        composeTestRule.onNodeWithText("Bukayo Saka").assertDoesNotExist()
        composeTestRule.onNodeWithText(nameLabel).assertDoesNotExist()
    }

    @Test
    fun reload_requestsUpdate() {
        var reloadCalls = 0

        setScreen(person = emptyPerson, onReload = { reloadCalls++ })

        composeTestRule.onNodeWithText(reloadLabel).performScrollTo().performClick()

        assertEquals(1, reloadCalls)
    }

    @Test
    fun loadingWithEmptyPerson_doesNotShowReloadOrPlayer() {
        setScreen(person = emptyPerson, isLoading = true)

        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("Bukayo Saka").assertDoesNotExist()
        composeTestRule.onNodeWithText(nameLabel).assertDoesNotExist()
    }

    @Test
    fun loadingWithPlayerOnScreen_keepsPlayerVisible() {
        setScreen(person = saka, isLoading = true)

        composeTestRule.onAllNodesWithText("Bukayo Saka").assertCountEquals(2)
        composeTestRule.onNodeWithText(nameLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(reloadLabel).assertDoesNotExist()
    }

    @Test
    fun loadedPlayer_showsNameAgeNationalityPositionAndNumber() {
        setScreen(person = saka)

        composeTestRule.onAllNodesWithText("Bukayo Saka").assertCountEquals(2)
        composeTestRule.onNodeWithText(nameLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("22").assertIsDisplayed()
        composeTestRule.onNodeWithText(ageLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("England").assertIsDisplayed()
        composeTestRule.onNodeWithText(nationalityLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(rightWingerLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(positionLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("7").assertIsDisplayed()
        composeTestRule.onNodeWithText(numberLabel).assertIsDisplayed()
    }

    @Test
    fun loadedPlayer_hidesNumberWhenMissing() {
        setScreen(person = saka.copy(shirtNumber = 0))

        composeTestRule.onNodeWithText(numberLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("7").assertDoesNotExist()
        composeTestRule.onNodeWithText(nameLabel).assertIsDisplayed()
    }

    @Test
    fun clickBack_requestsBack() {
        var backCalls = 0

        setScreen(person = saka, onBackClick = { backCalls++ })

        composeTestRule.onNodeWithContentDescription(backLabel).performClick()

        assertEquals(1, backCalls)
    }

    // region Helpers

    private val reloadLabel: String
        get() = composeTestRule.activity.getString(R.string.reload)

    private val backLabel: String
        get() = composeTestRule.activity.getString(R.string.back)

    private val nameLabel: String
        get() = composeTestRule.activity.getString(R.string.player_name)

    private val ageLabel: String
        get() = composeTestRule.activity.getString(R.string.player_age)

    private val nationalityLabel: String
        get() = composeTestRule.activity.getString(R.string.player_nationality)

    private val positionLabel: String
        get() = composeTestRule.activity.getString(R.string.player_position)

    private val numberLabel: String
        get() = composeTestRule.activity.getString(R.string.player_number)

    private val rightWingerLabel: String
        get() = composeTestRule.activity.getString(R.string.position_right_winger)

    private fun setScreen(
        person: Person,
        isLoading: Boolean = false,
        onReload: () -> Unit = {},
        onBackClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                GoalPulseTheme {
                    PersonInfoContent(
                        isLoading = isLoading,
                        person = person,
                        onReload = onReload,
                        onBackClick = onBackClick,
                    )
                }
            }
        }
    }

    // endregion
}

// region Test data

private val emptyPerson = Person(
    id = 44,
    name = "",
)

private val saka = Person(
    id = 44,
    name = "Bukayo Saka",
    age = "22",
    nationality = "England",
    position = PlayerPosition.RIGHT_WINGER,
    shirtNumber = 7,
    currentTeam = CurrentTeam(crest = "https://crests.football-data.org/57.svg"),
)

// endregion
