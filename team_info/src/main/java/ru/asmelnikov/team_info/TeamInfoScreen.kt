package ru.asmelnikov.team_info

import android.content.res.Configuration
import android.graphics.Color.parseColor
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.team_info.components.PaletteGenerator.convertImageUrlToBitmap
import ru.asmelnikov.team_info.components.PaletteGenerator.extractColorsFromBitmap
import ru.asmelnikov.team_info.components.SquadPagerList
import ru.asmelnikov.team_info.components.TeamInfoPage
import ru.asmelnikov.team_info.view_model.TeamInfoSideEffects
import ru.asmelnikov.team_info.view_model.TeamInfoViewModel
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.PagerTabRow
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.popUp
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun TeamInfoScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: TeamInfoViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is TeamInfoSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

            is TeamInfoSideEffects.BackClick -> appState.popUp()

        }
    }

    TeamInfoScreenContent(
        teamInfo = state.teamInfo,
        isLoading = state.isLoading,
        onBackClick = viewModel::onBackClick,
        colors = state.colorPalette,
        setColorPalette = viewModel::setColorPalette,
        onTeamInfoReload = viewModel::getTeamInfoFromRemoteToLocal
    )

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalToolbarApi::class)
@Composable
fun TeamInfoScreenContent(
    teamInfo: TeamInfo,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    colors: Map<String, String>,
    setColorPalette: (Map<String, String>) -> Unit,
    onTeamInfoReload: () -> Unit
) {

    val isMaterialColors = teamInfo.crest.endsWith(".svg")

    val context = LocalContext.current

    var launchedEffectTriggered by remember { mutableStateOf(false) }

    if (teamInfo.crest.isNotEmpty())
        LaunchedEffect(key1 = true) {
            try {
                val bitmap = convertImageUrlToBitmap(
                    imageUrl = teamInfo.crest,
                    context = context
                )
                if (bitmap != null) {
                    launchedEffectTriggered = true
                    setColorPalette(
                        extractColorsFromBitmap(
                            bitmap = bitmap
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    var vibrant by remember { mutableStateOf("#ffffff") }
    var lightMutedSwatch by remember { mutableStateOf("#ffffff") }
    var onDarkVibrant by remember { mutableStateOf("#ffffff") }

    if (colors.isNotEmpty() && launchedEffectTriggered)
        LaunchedEffect(key1 = true) {
            vibrant = colors["vibrant"] ?: ""
            lightMutedSwatch = colors["lightMuted"] ?: ""
            onDarkVibrant = colors["onDarkVibrant"] ?: ""
        }

    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        2
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .pin()
                        .background(
                            color = if (isMaterialColors) MaterialTheme.colorScheme.primaryContainer else Color(
                                parseColor(lightMutedSwatch)
                            )
                        )
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

                    SubComposeAsyncImageCommon(
                        imageUri = teamInfo.crest,
                        shape = RoundedCornerShape(0.dp),
                        size = imgSize
                    )

                }

                Text(
                    text = teamInfo.name,
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
                        ),
                    color = Color(parseColor(onDarkVibrant))
                )

            }) {

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .background(
                            if (isMaterialColors) MaterialTheme.colorScheme.primaryContainer else Color(
                                parseColor(lightMutedSwatch)
                            )
                        )
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.extraSmall1)
                ) {
                    if (isLoading) LinearProgressIndicator(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = if (isMaterialColors) MaterialTheme.colorScheme.primaryContainer else Color(
                            parseColor(lightMutedSwatch)
                        )
                    )
                }

                PagerTabRow(
                    tabTitles = listOf("Squad", "Info"),
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
                    pagerState = pagerState,
                    containerColor = if (isMaterialColors) MaterialTheme.colorScheme.background else Color(
                        parseColor(vibrant)
                    )
                )

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = pagerState,
                    beyondBoundsPageCount = 2,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (page) {
                        0 -> {
                            SquadPagerList(
                                teamInfo = teamInfo,
                                stickyHeaderColor = Color(parseColor(lightMutedSwatch)),
                                itemColor = Color(parseColor(vibrant)),
                                isMaterialColors = isMaterialColors,
                                isLoading = isLoading,
                                onReloadClick = onTeamInfoReload
                            )
                        }

                        1 -> {
                            TeamInfoPage(
                                teamInfo = teamInfo,
                                isLoading = isLoading,
                                onReloadClick = onTeamInfoReload
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

