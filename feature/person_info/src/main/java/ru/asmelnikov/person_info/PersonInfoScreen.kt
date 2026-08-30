package ru.asmelnikov.person_info

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.domain.models.getMockPlayer
import ru.asmelnikov.person_info.ContentState.Empty
import ru.asmelnikov.person_info.ContentState.List
import ru.asmelnikov.person_info.ContentState.Loading
import ru.asmelnikov.person_info.components.PersonTopBar
import ru.asmelnikov.person_info.components.TextItem
import ru.asmelnikov.person_info.view_model.PersonSideEffects
import ru.asmelnikov.person_info.view_model.PersonViewModel
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.EmptyContent
import ru.asmelnikov.utils.composables.LoadingBall
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.MainBackVideo
import ru.asmelnikov.utils.navigation.popUp
import ru.asmelnikov.utils.ui.theme.GoalPulseTheme
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun PersonInfoScreen(
    appState: MainAppState,
    personId: String,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit,
    ) -> Unit,
    viewModel: PersonViewModel = koinViewModel(parameters = { parametersOf(personId) }),
) {
    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is PersonSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null,
            ) {}

            is PersonSideEffects.BackClick -> appState.popUp()
        }
    }
    PersonInfoContent(
        isLoading = state.isLoading,
        person = state.person,
        onReload = viewModel::getPersonFromRemote,
        onBackClick = viewModel::onBackClick,
    )
}

@Composable
fun PersonInfoContent(
    isLoading: Boolean,
    person: Person,
    onReload: () -> Unit,
    onBackClick: () -> Unit,
) {
    val videoBackdrop = rememberLayerBackdrop()
    val listBackdrop = rememberLayerBackdrop()
    val topBarBackdrop = rememberLayerBackdrop()
    val contentBackdrop = rememberCombinedBackdrop(videoBackdrop, listBackdrop)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        MainBackVideo(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(videoBackdrop),
            videoResId = R.raw.player_video,
            reverseVideoResId = R.raw.player_video_reverse,
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(),
            topBar = {
                PersonTopBar(
                    modifier = Modifier
                        .layerBackdrop(topBarBackdrop),
                    backdrop = contentBackdrop,
                    name = person.name,
                    teamUrl = person.currentTeam.crest,
                    onBackClick = onBackClick,
                )
            },
        ) { paddingValues ->
            val topInsets = paddingValues.calculateTopPadding()

            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = when {
                    isLoading && person.name.isEmpty() -> Loading
                    !isLoading && person.name.isEmpty() -> Empty
                    else -> List
                },
                label = "competitions_content",
            ) { state ->
                when (state) {
                    Loading -> {
                        LoadingBall(
                            modifier = Modifier.padding(top = topInsets),
                        )
                    }
                    Empty -> EmptyContent(
                        modifier = Modifier.padding(top = topInsets),
                        onReloadClick = onReload,
                    )

                    List -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .layerBackdrop(listBackdrop),
                            verticalArrangement = Arrangement.spacedBy(dimens.small3),
                            contentPadding = PaddingValues(
                                start = dimens.medium1,
                                end = dimens.medium1,
                                bottom = dimens.medium1,
                                top = topInsets + dimens.medium1,
                            ),
                        ) {
                            item {
                                TextItem(
                                    backdrop = videoBackdrop,
                                    title = stringResource(R.string.player_name),
                                    text = person.name,
                                )
                            }

                            item {
                                TextItem(
                                    backdrop = videoBackdrop,
                                    title = stringResource(R.string.player_age),
                                    text = person.age,
                                )
                            }

                            item {
                                TextItem(
                                    backdrop = videoBackdrop,
                                    title = stringResource(R.string.player_nationality),
                                    text = person.nationality,
                                )
                            }

                            item {
                                TextItem(
                                    backdrop = videoBackdrop,
                                    title = stringResource(R.string.player_position),
                                    text = stringResource(person.position.stringResId),
                                )
                            }

                            if (person.shirtNumber > 0) {
                                item {
                                    TextItem(
                                        backdrop = videoBackdrop,
                                        title = stringResource(R.string.player_number),
                                        text = person.shirtNumber.toString(),
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.navigationBarsPadding())
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class ContentState {
    Loading,
    Empty,
    List,
}

@Preview(showSystemUi = false, showBackground = false, locale = "ru")
@Composable
private fun PersonInfoContentPreview1() {
    GoalPulseTheme(darkTheme = true) {
        PersonInfoContent(
            isLoading = false,
            person = getMockPlayer(),
            onReload = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun PersonInfoContentPreview2() {
    GoalPulseTheme {
        PersonInfoContent(
            isLoading = false,
            person = getMockPlayer(),
            onReload = {},
            onBackClick = {},
        )
    }
}

@Preview(showSystemUi = false, showBackground = false, locale = "ru")
@Composable
private fun PersonInfoContentPreview3() {
    GoalPulseTheme(darkTheme = true) {
        PersonInfoContent(
            isLoading = true,
            person = getMockPlayer().copy(name = ""),
            onReload = {},
            onBackClick = {},
        )
    }
}

@Preview(showSystemUi = false, showBackground = false, locale = "ru")
@Composable
private fun PersonInfoContentPreview4() {
    GoalPulseTheme(darkTheme = true) {
        PersonInfoContent(
            isLoading = false,
            person = getMockPlayer().copy(name = ""),
            onReload = {},
            onBackClick = {},
        )
    }
}
