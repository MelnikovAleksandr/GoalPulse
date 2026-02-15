package ru.asmelnikov.competition_standings.di

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel

val competitionStandingsModule = module {
    viewModel { (savedStateHandle: SavedStateHandle) ->
        CompetitionStandingsViewModel(
            standingsRepository = get(),
            stringResourceProvider = get(),
            savedStateHandle = savedStateHandle
        )
    }
}