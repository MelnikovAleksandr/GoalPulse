package ru.asmelnikov.team_info.di

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.team_info.view_model.TeamInfoViewModel

val teamInfoModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        TeamInfoViewModel(
            teamRepository = get(),
            stringResourceProvider = get(),
            savedStateHandle = savedStateHandle,
            standingsRepository = get(),
            newsRepository = get(),
            colorGenerator = get()
        )
    }
}