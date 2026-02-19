package ru.asmelnikov.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity

interface StandingsRealmOptions {

    suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity)

    fun getStandingsFlowById(compId: String): Flow<List<CompetitionStandingsEntity>>

    suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity)

    fun getScorersFlowById(compId: String): Flow<List<CompetitionScorersEntity>>

    suspend fun upsertMatchesFromRemoteToLocal(matches: CompetitionMatchesEntity)

    fun getMatchesFlowById(compId: String): Flow<List<CompetitionMatchesEntity>>

    class RealmOptionsImpl(private val realm: Realm) : StandingsRealmOptions {
        override suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(standings, UpdatePolicy.ALL)
                }
            }
        }

        override fun getStandingsFlowById(compId: String): Flow<List<CompetitionStandingsEntity>> {
            return realm.query<CompetitionStandingsEntity>().asFlow().map { it.list }
                .flowOn(Dispatchers.IO)
        }

        override suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(comp, UpdatePolicy.ALL)
                }
            }
        }

        override fun getScorersFlowById(compId: String): Flow<List<CompetitionScorersEntity>> {
            return realm.query<CompetitionScorersEntity>().asFlow().map { it.list }
                .flowOn(Dispatchers.IO)
        }


        override suspend fun upsertMatchesFromRemoteToLocal(matches: CompetitionMatchesEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(matches, UpdatePolicy.ALL)
                }
            }
        }

        override fun getMatchesFlowById(compId: String): Flow<List<CompetitionMatchesEntity>> {
            return realm.query<CompetitionMatchesEntity>().asFlow().map { it.list }
                .flowOn(Dispatchers.IO)
        }
    }
}