package ru.asmelnikov.competition_standings.di

import androidx.lifecycle.SavedStateHandle
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel

val competitionStandingsModule = module {
    viewModel { (compId: String, compUrl: String, savedStateHandle: SavedStateHandle) ->
        CompetitionStandingsViewModel(
            standingsRepository = get(),
            matchCalendarRepository = get(),
            stringResourceProvider = get(),
            compId = compId,
            compUrl = compUrl
        )
    }
}