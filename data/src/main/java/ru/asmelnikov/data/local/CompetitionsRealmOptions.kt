package ru.asmelnikov.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.CompetitionEntity

interface CompetitionsRealmOptions {

    suspend fun upsertCompetitionsDataFromRemoteToLocal(competitions: List<CompetitionEntity>)

    fun getCompetitionsFlowFromLocal(): Flow<List<CompetitionEntity>>

    class RealmOptionsImpl(private val realm: Realm) : CompetitionsRealmOptions {

        override suspend fun upsertCompetitionsDataFromRemoteToLocal(
            competitions: List<CompetitionEntity>,
        ) = withContext(Dispatchers.IO) {
            realm.write {
                val ids = competitions.map { it.id }

                delete(query<CompetitionEntity>("NOT(id IN $0)", ids).find())

                competitions.forEach {
                    copyToRealm(it, UpdatePolicy.ALL)
                }
            }
        }

        override fun getCompetitionsFlowFromLocal(): Flow<List<CompetitionEntity>> =
            realm.query<CompetitionEntity>().asFlow().map {
                it.list
            }.flowOn(Dispatchers.IO)
    }
}
