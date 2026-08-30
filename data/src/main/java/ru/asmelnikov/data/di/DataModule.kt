package ru.asmelnikov.data.di

import io.realm.kotlin.Configuration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.api.NewsApi
import ru.asmelnikov.data.calendar.ContentResolverMatchCalendarStore
import ru.asmelnikov.data.local.CompetitionsRealmOptions
import ru.asmelnikov.data.local.StandingsRealmOptions
import ru.asmelnikov.data.local.TeamInfoRealmOptions
import ru.asmelnikov.data.repository.CompetitionStandingsRepositoryImpl
import ru.asmelnikov.data.repository.CompetitionsRepositoryImpl
import ru.asmelnikov.data.repository.MatchCalendarRepositoryImpl
import ru.asmelnikov.data.repository.NewsRepositoryImpl
import ru.asmelnikov.data.repository.PersonRepositoryImpl
import ru.asmelnikov.data.repository.TeamInfoRepositoryImpl
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.CompetitionsRepository
import ru.asmelnikov.domain.repository.MatchCalendarRepository
import ru.asmelnikov.domain.repository.NewsRepository
import ru.asmelnikov.domain.repository.PersonRepository
import ru.asmelnikov.domain.repository.TeamInfoRepository

private const val FOOTBALL_API_URL = "https://api.football-data.org/v4/"
private const val NEWS_API_URL = "https://newsapi.org/v2/"
private const val DB_NAME = "goal_pulse.realm"
private const val SCHEMA_VERSION = 1L

val dataModule = module {

    single<Configuration> {
        RealmConfiguration.Builder(schema = entities)
            .name(DB_NAME)
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(SCHEMA_VERSION)
            .build()
    }

    single<Realm> { Realm.open(configuration = get()) }

    factory<RetrofitErrorsHandler> { RetrofitErrorsHandler.RetrofitErrorsHandlerImpl() }

    single<CompetitionsRealmOptions> { CompetitionsRealmOptions.RealmOptionsImpl(realm = get()) }

    single<StandingsRealmOptions> { StandingsRealmOptions.RealmOptionsImpl(realm = get()) }

    single<TeamInfoRealmOptions> { TeamInfoRealmOptions.RealmOptionsImpl(realm = get()) }

    single<OkHttpClient> { okHttp() }

    single<MoshiConverterFactory> { moshiConverterFactory() }

    single<Retrofit>(named(FOOTBALL_API_URL)) { retrofit(get(), get(), FOOTBALL_API_URL) }

    single<Retrofit>(named(NEWS_API_URL)) { retrofit(get(), get(), NEWS_API_URL) }

    single<FootballApi> { get<Retrofit>(named(FOOTBALL_API_URL)).create(FootballApi::class.java) }

    single<NewsApi> { get<Retrofit>(named(NEWS_API_URL)).create(NewsApi::class.java) }

    single<NewsRepository> { NewsRepositoryImpl(newsApi = get(), retrofitErrorsHandler = get()) }

    single<CompetitionsRepository> {
        CompetitionsRepositoryImpl(
            footballApi = get(),
            realmOptions = get(),
            retrofitErrorsHandler = get(),
        )
    }

    single<CompetitionStandingsRepository> {
        CompetitionStandingsRepositoryImpl(
            footballApi = get(),
            realmOptions = get(),
            retrofitErrorsHandler = get(),
        )
    }

    single<TeamInfoRepository> {
        TeamInfoRepositoryImpl(
            footballApi = get(),
            realmOptions = get(),
            retrofitErrorsHandler = get(),
        )
    }

    single<PersonRepository> {
        PersonRepositoryImpl(
            footballApi = get(),
            retrofitErrorsHandler = get(),
        )
    }

    single<MatchCalendarRepository> {
        val context = androidContext()
        MatchCalendarRepositoryImpl(
            appPackageName = context.packageName,
            store = ContentResolverMatchCalendarStore(context),
        )
    }
}

private fun moshiConverterFactory(): MoshiConverterFactory =
    MoshiConverterFactory.create()

private fun okHttp(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .build()

private fun retrofit(
    moshiConverterFactory: MoshiConverterFactory,
    okHttpClient: OkHttpClient,
    url: String,
) = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(moshiConverterFactory)
    .client(okHttpClient)
    .build()

private fun loggingInterceptor() =
    HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
