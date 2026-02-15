package ru.asmelnikov.person_info.di

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.person_info.view_model.PersonViewModel

val personModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        PersonViewModel(
            savedStateHandle = savedStateHandle,
            repository = get(),
            stringResourceProvider = get()
        )
    }
}