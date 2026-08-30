package ru.asmelnikov.goalpulse

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.asmelnikov.competition_standings.di.competitionStandingsModule
import ru.asmelnikov.competitions_main.di.competitionsScreenModule
import ru.asmelnikov.data.di.dataModule
import ru.asmelnikov.person_info.di.personModule
import ru.asmelnikov.team_info.di.teamInfoModule
import ru.asmelnikov.utils.di.utilsModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    competitionsScreenModule,
                    competitionStandingsModule,
                    teamInfoModule,
                    personModule,
                    utilsModule,
                ),
            )
        }
    }
}
