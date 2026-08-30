package ru.asmelnikov.team_info.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.team_info.view_model.TeamInfoViewModel

val teamInfoModule = module {

    viewModel { (teamId: String) ->
        TeamInfoViewModel(
            teamRepository = get(),
            stringResourceProvider = get(),
            standingsRepository = get(),
            newsRepository = get(),
            matchCalendarRepository = get(),
            teamId = teamId,
        )
    }
}
