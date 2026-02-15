package ru.asmelnikov.competitions_main.di

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenViewModel

val competitionsScreenModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        CompetitionsScreenViewModel(
            footballRepository = get(),
            stringResourceProvider = get(),
            savedStateHandle = savedStateHandle
        )
    }
}